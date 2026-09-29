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

    @Test
    fun `generated questions use different prompts and multiple question styles`() {
        val summary = WikiSummaryDto(
            title = "Ada Lovelace",
            pageId = 1,
            description = "matematica e pioniera della programmazione",
            extract = "Ada Lovelace è stata una matematica inglese. È considerata la prima programmatrice della storia per il suo lavoro sulla macchina analitica. I suoi appunti descrivevano algoritmi capaci di elaborare simboli oltre ai numeri.",
            lang = "it"
        )

        val questions = generator.generateQuestions(summary)

        assertEquals(questions.size, questions.map { it.text }.distinct().size)
        assertTrue(questions.map { it.type }.distinct().size >= 2)
        assertTrue(questions.map { it.options[it.correctOptionIndex] }.distinct().size >= 2)
    }

    @Test
    fun `same article keeps a stable varied question set`() {
        val summary = WikiSummaryDto(
            title = "Basket",
            pageId = 2,
            description = "sport di squadra con palla",
            extract = "Il basket è uno sport di squadra nato negli Stati Uniti. Due squadre cercano di segnare in un canestro avversario. La partita richiede coordinazione, velocità e lettura dello spazio.",
            lang = "it"
        )

        val first = generator.generateQuestions(summary).map { it.text to it.options }
        val second = generator.generateQuestions(summary).map { it.text to it.options }

        assertEquals(first, second)
    }

    @Test
    fun `fallback context question asks about a concrete fact`() {
        val summary = WikiSummaryDto(
            title = "Basket",
            pageId = 3,
            description = "sport di squadra",
            extract = "Il basket è uno sport di squadra nato negli Stati Uniti. Due squadre cercano di segnare in un canestro avversario.",
            lang = "it"
        )

        val questions = generator.generateQuestions(summary)

        assertTrue(questions.none { it.text.contains("natura generale", ignoreCase = true) })
        assertTrue(questions.any { it.text.contains("fatto concreto", ignoreCase = true) })
    }

    @Test
    fun `English question set uses English prompts and answer labels`() {
        val summary = WikiSummaryDto(
            title = "Computer",
            pageId = 4,
            description = "a machine that processes information",
            extract = "A computer is a machine that processes information. It follows algorithms to transform input into output.",
            lang = "en"
        )

        val questions = generator.generateQuestions(summary, "en")

        assertTrue(questions.all { !it.text.contains("Qual", ignoreCase = true) })
        assertTrue(questions.all { it.options.none { option -> option.contains("Vero") || option.contains("Falso") } })
        assertTrue(questions.any { it.options.contains("True") && it.options.contains("False") })
        assertTrue(questions.all { !it.explanation.contains("La voce", ignoreCase = true) })
    }
}
