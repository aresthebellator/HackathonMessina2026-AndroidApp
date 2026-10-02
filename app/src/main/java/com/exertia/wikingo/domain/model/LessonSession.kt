package com.exertia.wikingo.domain.model

data class LessonSession(
    val id: String,
    val topicTitle: String,
    val topicDescription: String,
    val topicExtract: String,
    val thumbnailUrl: String?,
    val wikiUrl: String,
    val pageId: Long,
    val questions: List<Question>,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val isCompleted: Boolean = false
) {
    val totalQuestions: Int get() = questions.size

    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (totalQuestions > 0) currentQuestionIndex.toFloat() / totalQuestions else 0f

    val accuracyPercentage: Int
        get() = if (totalQuestions > 0) ((score.toFloat() / totalQuestions) * 100).toInt() else 0

    val xpEarned: Int
        get() = (score * 10) + if (accuracyPercentage >= 80) 15 else 5
}
