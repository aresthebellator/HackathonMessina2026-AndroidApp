package com.exertia.wikingo.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UnitsDataTest {

    @Test
    fun `UNITS_DATA contains exactly 10 sections spanning 100 lessons`() {
        assertEquals(10, UNITS_DATA.size)
        assertEquals(1, UNITS_DATA.first().startLesson)
        assertEquals(100, UNITS_DATA.last().endLesson)

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
        assertEquals(7, getUnitForLesson(61).id)
        assertEquals(8, getUnitForLesson(80).id)
        assertEquals(9, getUnitForLesson(81).id)
        assertEquals(10, getUnitForLesson(100).id)
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
    fun `LESSON_TITLES contains all 100 lesson titles`() {
        assertEquals(100, LESSON_TITLES.size)
        for (i in 1..100) {
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

    @Test
    fun `every lesson has a predefined Italian and English topic`() {
        val italianTopics = (1..MAX_LESSON_NUMBER).map { getLessonTopic(it, "it") }
        val englishTopics = (1..MAX_LESSON_NUMBER).map { getLessonTopic(it, "en") }

        assertEquals(MAX_LESSON_NUMBER, italianTopics.distinct().size)
        assertEquals(MAX_LESSON_NUMBER, englishTopics.distinct().size)
        assertTrue(italianTopics.all { it.isNotBlank() })
        assertTrue(englishTopics.all { it.isNotBlank() })
        assertEquals("Sport", getUnitForLesson(81).topic.substringBefore(","))
        assertTrue(getLessonTopic(91, "en").contains("Algorithm"))
    }
}
