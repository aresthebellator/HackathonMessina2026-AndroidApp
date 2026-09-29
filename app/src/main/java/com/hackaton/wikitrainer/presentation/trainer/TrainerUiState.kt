package com.hackaton.wikitrainer.presentation.trainer

import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.Question
import com.hackaton.wikitrainer.domain.model.UserStats

data class AnswerFeedback(
    val isCorrect: Boolean,
    val selectedOptionIndex: Int,
    val correctOptionIndex: Int,
    val correctAnswerText: String,
    val explanation: String
)

sealed interface TrainerUiState {

    data class Loading(
        val message: String = "Recupero un nuovo articolo da Wikipedia..."
    ) : TrainerUiState

    data class QuestionState(
        val session: LessonSession,
        val currentQuestion: Question,
        val questionNumber: Int,
        val totalQuestions: Int,
        val progress: Float,
        val selectedOptionIndex: Int? = null,
        val feedback: AnswerFeedback? = null,
        val userStats: UserStats,
        val hearts: Int = 10,
        val isSoundEnabled: Boolean = true
    ) : TrainerUiState {
        val isAnswerChecked: Boolean get() = feedback != null
        val canCheckAnswer: Boolean get() = selectedOptionIndex != null && feedback == null
    }

    data class CompleteState(
        val session: LessonSession,
        val score: Int,
        val totalQuestions: Int,
        val accuracy: Int,
        val xpEarned: Int,
        val userStats: UserStats,
        val topicTitle: String,
        val topicDescription: String,
        val topicExtract: String,
        val thumbnailUrl: String?,
        val wikiUrl: String
    ) : TrainerUiState

    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : TrainerUiState
}
