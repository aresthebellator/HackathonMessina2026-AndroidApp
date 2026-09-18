package com.hackaton.wikitrainer.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.wikitrainer.domain.usecase.GetTopicHistoryUseCase
import com.hackaton.wikitrainer.domain.usecase.GetUserStatsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    getTopicHistoryUseCase: GetTopicHistoryUseCase,
    getUserStatsUseCase: GetUserStatsUseCase
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = combine(
        getTopicHistoryUseCase(),
        getUserStatsUseCase()
    ) { topics, stats ->
        HistoryUiState.Success(topics = topics, stats = stats)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState.Loading
    )
}
