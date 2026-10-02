package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.core.network.NetworkResult
import com.exertia.wikingo.domain.model.LessonSession
import com.exertia.wikingo.domain.repository.LessonRepository

class GetRandomLessonUseCase(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(language: String = "it"): NetworkResult<LessonSession> {
        return repository.getRandomLesson(language)
    }
}
