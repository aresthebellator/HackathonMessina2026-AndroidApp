package com.hackaton.wikitrainer.presentation.history

import com.hackaton.wikitrainer.domain.model.TopicHistory
import com.hackaton.wikitrainer.domain.model.UserStats

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(
        val topics: List<TopicHistory>,
        val stats: UserStats
    ) : HistoryUiState
}
