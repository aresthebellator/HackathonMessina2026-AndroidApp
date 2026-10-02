package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.domain.model.TopicHistory
import com.exertia.wikingo.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow

class GetTopicHistoryUseCase(
    private val repository: LessonRepository
) {
    operator fun invoke(): Flow<List<TopicHistory>> {
        return repository.getTopicHistory()
    }
}
