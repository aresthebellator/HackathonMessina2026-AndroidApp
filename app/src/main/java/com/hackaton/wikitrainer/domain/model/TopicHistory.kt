package com.hackaton.wikitrainer.domain.model

data class TopicHistory(
    val id: Long,
    val title: String,
    val description: String,
    val extract: String,
    val thumbnailUrl: String?,
    val wikiUrl: String,
    val score: Int,
    val totalQuestions: Int,
    val completedAt: Long,
    val xpEarned: Int
) {
    val accuracy: Int
        get() = if (totalQuestions > 0) ((score.toFloat() / totalQuestions) * 100).toInt() else 0
}
