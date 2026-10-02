package com.exertia.wikingo.presentation.trainer

sealed interface TrainerUiEvent {
    data class SelectOption(val index: Int) : TrainerUiEvent
    data object CheckAnswer : TrainerUiEvent
    data object NextQuestion : TrainerUiEvent
    data object StartNewLesson : TrainerUiEvent
    data object Retry : TrainerUiEvent
    data object ToggleSound : TrainerUiEvent
}
