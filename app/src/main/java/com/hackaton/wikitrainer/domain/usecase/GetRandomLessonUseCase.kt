package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.repository.LessonRepository

class GetRandomLessonUseCase(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(language: String = "it"): NetworkResult<LessonSession> {
        return repository.getRandomLesson(language)
    }
}
