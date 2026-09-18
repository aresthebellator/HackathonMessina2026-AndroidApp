package com.hackaton.wikitrainer.domain.usecase

import com.hackaton.wikitrainer.domain.model.TopicHistory
import com.hackaton.wikitrainer.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow

class GetTopicHistoryUseCase(
    private val repository: LessonRepository
) {
    operator fun invoke(): Flow<List<TopicHistory>> {
        return repository.getTopicHistory()
    }
}
