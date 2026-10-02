package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.domain.model.LessonSession
import com.exertia.wikingo.domain.repository.LessonRepository

class CompleteLessonUseCase(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(session: LessonSession): Long {
        val completedSession = session.copy(isCompleted = true)
        return repository.saveCompletedLesson(completedSession)
    }
}
