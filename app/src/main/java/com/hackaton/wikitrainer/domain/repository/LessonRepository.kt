package com.hackaton.wikitrainer.domain.repository

import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.TopicHistory
import com.hackaton.wikitrainer.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    suspend fun getRandomLesson(language: String = "it"): NetworkResult<LessonSession>
    suspend fun getLessonForTopic(topic: String, language: String = "it"): NetworkResult<LessonSession>
    suspend fun saveCompletedLesson(session: LessonSession): Long
    fun getTopicHistory(): Flow<List<TopicHistory>>
    fun getUserStats(): Flow<UserStats>
    suspend fun getTopicById(id: Long): TopicHistory?
}
