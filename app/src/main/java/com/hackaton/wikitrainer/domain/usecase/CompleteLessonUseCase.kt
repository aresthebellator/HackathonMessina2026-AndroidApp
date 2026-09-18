package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.repository.LessonRepository

class CompleteLessonUseCase(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(session: LessonSession): Long {
        val completedSession = session.copy(isCompleted = true)
        return repository.saveCompletedLesson(completedSession)
    }
}
