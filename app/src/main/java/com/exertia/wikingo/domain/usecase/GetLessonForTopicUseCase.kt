package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.core.network.NetworkResult
import com.exertia.wikingo.domain.model.LessonSession
import com.exertia.wikingo.domain.repository.LessonRepository

class GetLessonForTopicUseCase(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(topic: String, language: String = "it"): NetworkResult<LessonSession> {
        val topicResult = repository.getLessonForTopic(topic, language)
        return when (topicResult) {
            is NetworkResult.Success -> topicResult
            is NetworkResult.Offline -> topicResult
            is NetworkResult.Error -> {
                // Graceful fallback to random lesson if specific topic fails
                repository.getRandomLesson(language)
            }
        }
    }
}
