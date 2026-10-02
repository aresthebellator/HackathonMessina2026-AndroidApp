package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.domain.model.LessonSession

data class AnswerEvaluation(
    val isCorrect: Boolean,
    val selectedOptionIndex: Int,
    val correctOptionIndex: Int,
    val correctAnswerText: String,
    val explanation: String,
    val updatedSession: LessonSession
)

class SubmitAnswerUseCase {
    operator fun invoke(session: LessonSession, selectedOptionIndex: Int): AnswerEvaluation {
        val currentQuestion = session.currentQuestion
            ?: throw IllegalStateException("Nessuna domanda attiva nella sessione corrente.")

        val isCorrect = currentQuestion.isAnswerCorrect(selectedOptionIndex)
        val newScore = if (isCorrect) session.score + 1 else session.score

        val updatedSession = session.copy(
            score = newScore
        )

        return AnswerEvaluation(
            isCorrect = isCorrect,
            selectedOptionIndex = selectedOptionIndex,
            correctOptionIndex = currentQuestion.correctOptionIndex,
            correctAnswerText = currentQuestion.correctAnswerText,
            explanation = currentQuestion.explanation,
            updatedSession = updatedSession
        )
    }
}
