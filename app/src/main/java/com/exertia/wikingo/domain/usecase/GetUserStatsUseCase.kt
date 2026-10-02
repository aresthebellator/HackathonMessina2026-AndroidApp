package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.domain.model.UserStats
import com.exertia.wikingo.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val repository: LessonRepository
) {
    operator fun invoke(): Flow<UserStats> {
        return repository.getUserStats()
    }
}
