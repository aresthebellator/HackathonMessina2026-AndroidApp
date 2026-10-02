package com.exertia.wikingo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exertia.wikingo.domain.model.TopicHistory

@Entity(tableName = "topic_history")
data class TopicHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pageId: Long,
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
    fun toDomain(): TopicHistory = TopicHistory(
        id = id,
        title = title,
        description = description,
        extract = extract,
        thumbnailUrl = thumbnailUrl,
        wikiUrl = wikiUrl,
        score = score,
        totalQuestions = totalQuestions,
        completedAt = completedAt,
        xpEarned = xpEarned
    )
}
