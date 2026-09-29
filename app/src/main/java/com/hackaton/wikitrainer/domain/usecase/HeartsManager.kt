package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.domain.model.HeartState
import java.util.concurrent.TimeUnit

interface HeartStorage {
    fun getCount(): Int?
    fun getLastChangeMillis(): Long?
    fun save(count: Int, lastChangeMillis: Long)
}

class HeartsManager(
    private val storage: HeartStorage,
    private val nowMillis: () -> Long = { System.currentTimeMillis() }
) {
    fun getState(): HeartState {
        val now = nowMillis()
        val storedCount = storage.getCount() ?: MAX_HEARTS
        val storedLastChange = storage.getLastChangeMillis() ?: now
        val refreshed = refresh(storedCount.coerceIn(0, MAX_HEARTS), storedLastChange, now)
        if (refreshed.count != storedCount || refreshed.lastChangeMillis != storedLastChange) {
            storage.save(refreshed.count, refreshed.lastChangeMillis)
        }
        return refreshed.toHeartState()
    }

    fun consumeHeart(): HeartState {
        val current = getState()
        if (current.count == 0) return current

        val now = nowMillis()
        val newCount = current.count - 1
        val lastChange = if (current.count == MAX_HEARTS) now else {
            storage.getLastChangeMillis() ?: now
        }
        storage.save(newCount, lastChange)
        return refresh(newCount, lastChange, now).toHeartState()
    }

    private fun refresh(count: Int, lastChangeMillis: Long, now: Long): StoredState {
        if (count >= MAX_HEARTS) return StoredState(MAX_HEARTS, now)

        val elapsed = (now - lastChangeMillis).coerceAtLeast(0L)
        val recharged = (elapsed / RECHARGE_INTERVAL_MILLIS).toInt()
        if (recharged == 0) return StoredState(count, lastChangeMillis)

        val refreshedCount = (count + recharged).coerceAtMost(MAX_HEARTS)
        val refreshedLastChange = if (refreshedCount == MAX_HEARTS) {
            now
        } else {
            lastChangeMillis + recharged * RECHARGE_INTERVAL_MILLIS
        }
        return StoredState(refreshedCount, refreshedLastChange)
    }

    private data class StoredState(
        val count: Int,
        val lastChangeMillis: Long
    ) {
        fun toHeartState(): HeartState {
            return HeartState(
                count = count,
                nextRechargeAtMillis = if (count < MAX_HEARTS) {
                    lastChangeMillis + RECHARGE_INTERVAL_MILLIS
                } else {
                    null
                }
            )
        }
    }

    companion object {
        const val MAX_HEARTS = 10
        val RECHARGE_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(2)
    }
}
