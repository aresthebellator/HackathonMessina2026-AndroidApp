package com.exertia.wikingo.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class HeartsManagerTest {

    private lateinit var storage: FakeHeartStorage
    private var now = 0L
    private lateinit var manager: HeartsManager

    @Before
    fun setUp() {
        storage = FakeHeartStorage()
        manager = HeartsManager(storage) { now }
    }

    @Test
    fun `starts with ten hearts and no recharge scheduled`() {
        val state = manager.getState()

        assertEquals(10, state.count)
        assertNull(state.nextRechargeAtMillis)
    }

    @Test
    fun `consuming a heart leaves nine and schedules recharge in two hours`() {
        val state = manager.consumeHeart()

        assertEquals(9, state.count)
        assertEquals(TWO_HOURS, state.nextRechargeAtMillis)
    }

    @Test
    fun `one heart recharges after two hours`() {
        manager.consumeHeart()
        now = TWO_HOURS

        val state = manager.getState()

        assertEquals(10, state.count)
        assertNull(state.nextRechargeAtMillis)
    }

    @Test
    fun `elapsed intervals recharge multiple hearts but never exceed ten`() {
        repeat(10) { manager.consumeHeart() }
        now = 3 * TWO_HOURS

        val state = manager.getState()

        assertEquals(3, state.count)
        assertEquals(4 * TWO_HOURS, state.nextRechargeAtMillis)
    }

    @Test
    fun `consuming at zero does not make the count negative`() {
        repeat(10) { manager.consumeHeart() }

        val state = manager.consumeHeart()

        assertEquals(0, state.count)
    }

    private class FakeHeartStorage : HeartStorage {
        private var storedCount: Int? = null
        private var storedLastChangeMillis: Long? = null

        override fun getCount(): Int? = storedCount

        override fun getLastChangeMillis(): Long? = storedLastChangeMillis

        override fun save(count: Int, lastChangeMillis: Long) {
            storedCount = count
            storedLastChangeMillis = lastChangeMillis
        }
    }

    private companion object {
        const val TWO_HOURS = 2 * 60 * 60 * 1000L
    }
}
