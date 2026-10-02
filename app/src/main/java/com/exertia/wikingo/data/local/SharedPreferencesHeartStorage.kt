package com.exertia.wikingo.data.local

import android.content.SharedPreferences
import com.exertia.wikingo.domain.usecase.HeartStorage

class SharedPreferencesHeartStorage(
    private val preferences: SharedPreferences
) : HeartStorage {
    override fun getCount(): Int? {
        return if (preferences.contains(KEY_COUNT)) {
            preferences.getInt(KEY_COUNT, 10)
        } else {
            null
        }
    }

    override fun getLastChangeMillis(): Long? {
        return if (preferences.contains(KEY_LAST_CHANGE)) {
            preferences.getLong(KEY_LAST_CHANGE, 0L)
        } else {
            null
        }
    }

    override fun save(count: Int, lastChangeMillis: Long) {
        preferences.edit()
            .putInt(KEY_COUNT, count)
            .putLong(KEY_LAST_CHANGE, lastChangeMillis)
            .apply()
    }

    private companion object {
        const val KEY_COUNT = "hearts_count"
        const val KEY_LAST_CHANGE = "hearts_last_change_millis"
    }
}
