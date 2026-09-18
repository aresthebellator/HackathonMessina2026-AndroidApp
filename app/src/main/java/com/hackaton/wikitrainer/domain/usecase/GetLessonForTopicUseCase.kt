package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.repository.LessonRepository

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
