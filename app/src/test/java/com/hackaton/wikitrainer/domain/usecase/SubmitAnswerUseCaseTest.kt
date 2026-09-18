package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.Question
import com.hackaton.wikitrainer.domain.model.QuestionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitAnswerUseCaseTest {

    private lateinit var useCase: SubmitAnswerUseCase

    @Before
    fun setUp() {
        useCase = SubmitAnswerUseCase()
    }

    private fun createSampleSession(): LessonSession {
        val question1 = Question(
            id = "q1",
            text = "Qual è la capitale d'Italia?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = listOf("Milano", "Roma", "Napoli", "Torino"),
            correctOptionIndex = 1,
            explanation = "Roma è la capitale d'Italia dal 1871."
        )
        val question2 = Question(
            id = "q2",
            text = "Vero o Falso: Roma fu fondata nel 753 a.C.",
            type = QuestionType.TRUE_FALSE,
            options = listOf("Vero", "Falso"),
            correctOptionIndex = 0,
            explanation = "La tradizione fa risalire la fondazione al 21 aprile 753 a.C."
        )

        return LessonSession(
            id = "test-session",
            topicTitle = "Roma",
            topicDescription = "Capitale d'Italia",
            topicExtract = "Roma è la capitale d'Italia...",
            thumbnailUrl = null,
            wikiUrl = "https://it.wikipedia.org/wiki/Roma",
            pageId = 100,
            questions = listOf(question1, question2),
            currentQuestionIndex = 0,
            score = 0
        )
    }

    @Test
    fun `submitting correct answer returns isCorrect true and increments score`() {
        val session = createSampleSession()

        val result = useCase(session, selectedOptionIndex = 1) // Correct index

        assertTrue(result.isCorrect)
        assertEquals(1, result.updatedSession.score)
        assertEquals("Roma", result.correctAnswerText)
    }

    @Test
    fun `submitting wrong answer returns isCorrect false and retains previous score`() {
        val session = createSampleSession()

        val result = useCase(session, selectedOptionIndex = 0) // Wrong index (Milano)

        assertFalse(result.isCorrect)
        assertEquals(0, result.updatedSession.score)
        assertEquals("Roma", result.correctAnswerText)
        assertEquals("Roma è la capitale d'Italia dal 1871.", result.explanation)
    }
}
