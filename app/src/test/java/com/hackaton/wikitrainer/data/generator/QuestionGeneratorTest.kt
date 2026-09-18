package com.hackaton.wikitrainer.data.generator

import com.hackaton.wikitrainer.data.remote.dto.WikiSummaryDto
import com.hackaton.wikitrainer.domain.model.QuestionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuestionGeneratorTest {

    private lateinit var generator: QuestionGenerator

    @Before
    fun setUp() {
        generator = QuestionGenerator()
    }

    @Test
    fun `generateQuestions produces between 5 and 7 questions for standard Wikipedia summary`() {
        val sampleSummary = WikiSummaryDto(
            title = "Galileo Galilei",
            pageId = 1204,
            description = "fisico, astronomo, filosofo e matematico italiano",
            extract = "Galileo Galilei è stato un fisico, astronomo, filosofo, matematico e accademico italiano. Considerato il padre della scienza moderna, ha svolto un ruolo primario nella rivoluzione scientifica. Tra i suoi contributi figurano il perfezionamento del cannocchiale e le importanti osservazioni astronomiche del 1609.",
            lang = "it"
        )

        val questions = generator.generateQuestions(sampleSummary)

        assertTrue("Expected between 5 and 7 questions, but got ${questions.size}", questions.size in 5..7)
    }

    @Test
    fun `multiple choice questions have 4 distinct options and valid correct index`() {
        val sampleSummary = WikiSummaryDto(
            title = "Colosseo",
            pageId = 3280,
            description = "anfiteatro romano situato nel centro di Roma",
            extract = "Il Colosseo è il più grande anfiteatro romano del mondo. Edificato a partire dal 70 d.C. sotto la dinastia Flavia, fu utilizzato per spettacoli gladiatori. Nel 1980 è stato inserito tra i Patrimoni dell'umanità UNESCO.",
            lang = "it"
        )

        val questions = generator.generateQuestions(sampleSummary)
        val mcQuestions = questions.filter { it.type == QuestionType.MULTIPLE_CHOICE }

        assertTrue(mcQuestions.isNotEmpty())
        for (q in mcQuestions) {
            assertEquals("Multiple choice must have 4 options", 4, q.options.size)
            assertTrue("Correct option index out of bounds: ${q.correctOptionIndex}", q.correctOptionIndex in 0..3)
            val correctText = q.options[q.correctOptionIndex]
            assertNotNull(correctText)
            assertFalse(correctText.isBlank())
        }
    }

    @Test
    fun `true false questions have Vero and Falso options`() {
        val sampleSummary = WikiSummaryDto(
            title = "Marte",
            pageId = 8912,
            description = "quarto pianeta del sistema solare",
            extract = "Marte è il quarto pianeta del sistema solare in ordine di distanza dal Sole. Viene chiamato il pianeta rosso a causa dell'ossido di ferro che ne ricopre la superficie.",
            lang = "it"
        )

        val questions = generator.generateQuestions(sampleSummary)
        val tfQuestions = questions.filter { it.type == QuestionType.TRUE_FALSE }

        assertTrue(tfQuestions.isNotEmpty())
        for (q in tfQuestions) {
            assertEquals(listOf("Vero", "Falso"), q.options)
            assertTrue(q.correctOptionIndex in 0..1)
            assertTrue(q.explanation.isNotBlank())
        }
    }

    @Test
    fun `offline fallback summary generates valid playable lesson`() {
        val offlineSummary = generator.getCuratedOfflineSummary()

        assertNotNull(offlineSummary.title)
        assertTrue(offlineSummary.title.isNotBlank())
        assertNotNull(offlineSummary.extract)

        val questions = generator.generateQuestions(offlineSummary)
        assertTrue(questions.size in 5..7)
    }
}
