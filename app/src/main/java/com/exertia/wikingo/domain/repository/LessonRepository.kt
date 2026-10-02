package com.exertia.wikingo.domain.repository

import com.exertia.wikingo.core.network.NetworkResult
import com.exertia.wikingo.domain.model.LessonSession
import com.exertia.wikingo.domain.model.TopicHistory
import com.exertia.wikingo.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    suspend fun getRandomLesson(language: String = "it"): NetworkResult<LessonSession>
    suspend fun getLessonForTopic(topic: String, language: String = "it"): NetworkResult<LessonSession>
    suspend fun saveCompletedLesson(session: LessonSession): Long
    fun getTopicHistory(): Flow<List<TopicHistory>>
    fun getUserStats(): Flow<UserStats>
    suspend fun getTopicById(id: Long): TopicHistory?
}
