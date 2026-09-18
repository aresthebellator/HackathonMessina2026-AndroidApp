package com.hackaton.wikitrainer.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UnitsDataTest {

    @Test
    fun `UNITS_DATA contains exactly 6 sections spanning 60 lessons`() {
        assertEquals(6, UNITS_DATA.size)
        assertEquals(1, UNITS_DATA.first().startLesson)
        assertEquals(60, UNITS_DATA.last().endLesson)

        UNITS_DATA.forEachIndexed { index, unit ->
            assertEquals(index + 1, unit.id)
            assertEquals(10, unit.endLesson - unit.startLesson + 1)
            assertTrue(unit.keywords.isNotEmpty())
            assertNotNull(unit.primaryColor)
            assertNotNull(unit.darkColor)
        }
    }

    @Test
    fun `getUnitForLesson returns correct unit for boundaries and checkpoints`() {
        assertEquals(1, getUnitForLesson(1).id)
        assertEquals(1, getUnitForLesson(10).id)
        assertEquals(2, getUnitForLesson(11).id)
        assertEquals(2, getUnitForLesson(20).id)
        assertEquals(3, getUnitForLesson(21).id)
        assertEquals(4, getUnitForLesson(35).id)
        assertEquals(5, getUnitForLesson(45).id)
        assertEquals(6, getUnitForLesson(60).id)
    }

    @Test
    fun `isCheckpointLesson flags every tenth lesson as boss checkpoint`() {
        assertTrue(isCheckpointLesson(10))
        assertTrue(isCheckpointLesson(20))
        assertTrue(isCheckpointLesson(30))
        assertTrue(isCheckpointLesson(40))
        assertTrue(isCheckpointLesson(50))
        assertTrue(isCheckpointLesson(60))

        assertFalse(isCheckpointLesson(1))
        assertFalse(isCheckpointLesson(5))
        assertFalse(isCheckpointLesson(9))
        assertFalse(isCheckpointLesson(14))
    }

    @Test
    fun `LESSON_TITLES contains all 60 lesson titles`() {
        assertEquals(60, LESSON_TITLES.size)
        for (i in 1..60) {
            val title = getLessonTitle(i)
            assertTrue("Title for lesson $i should not be empty", title.isNotBlank())
        }
    }

    @Test
    fun `getSerpentineOffset returns consistent wave pattern`() {
        val o1 = getSerpentineOffset(1)
        val o2 = getSerpentineOffset(2)
        val o3 = getSerpentineOffset(3)
        val o7 = getSerpentineOffset(7)

        assertEquals(0f, o1.value, 0.01f)
        assertTrue(o2.value < 0f)
        assertTrue(o3.value < o2.value)
        assertTrue(o7.value > 0f)
    }
}
