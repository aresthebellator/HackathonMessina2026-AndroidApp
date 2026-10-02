package com.exertia.wikingo.presentation.history

import com.exertia.wikingo.domain.model.TopicHistory
import com.exertia.wikingo.domain.model.UserStats

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(
        val topics: List<TopicHistory>,
        val stats: UserStats
    ) : HistoryUiState
}
