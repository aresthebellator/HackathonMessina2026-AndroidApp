package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val repository: LessonRepository
) {
    operator fun invoke(): Flow<UserStats> {
        return repository.getUserStats()
    }
}
