package com.hackaton.wikitrainer.presentation.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.wikitrainer.core.audio.SoundFeedbackManager
import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.usecase.CompleteLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetRandomLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetUserStatsUseCase
import com.hackaton.wikitrainer.domain.usecase.SubmitAnswerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrainerViewModel(
    private val getRandomLessonUseCase: GetRandomLessonUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase,
    getUserStatsUseCase: GetUserStatsUseCase,
    private val soundFeedbackManager: SoundFeedbackManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrainerUiState>(TrainerUiState.Loading())
    val uiState: StateFlow<TrainerUiState> = _uiState.asStateFlow()

    private val userStatsFlow: StateFlow<UserStats> = getUserStatsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStats(0, 0, 0, 0, "")
        )

    init {
        loadNewLesson()
    }

    fun onEvent(event: TrainerUiEvent) {
        when (event) {
            is TrainerUiEvent.SelectOption -> handleSelectOption(event.index)
            is TrainerUiEvent.CheckAnswer -> handleCheckAnswer()
            is TrainerUiEvent.NextQuestion -> handleNextQuestion()
            is TrainerUiEvent.StartNewLesson -> loadNewLesson()
            is TrainerUiEvent.Retry -> loadNewLesson()
            is TrainerUiEvent.ToggleSound -> handleToggleSound()
        }
    }

    fun loadNewLesson(language: String = "it") {
        viewModelScope.launch {
            _uiState.value = TrainerUiState.Loading("Esploro Wikipedia per una nuova lezione...")

            when (val result = getRandomLessonUseCase(language)) {
                is NetworkResult.Success -> {
                    val session = result.data
                    val firstQuestion = session.currentQuestion
                    if (firstQuestion != null) {
                        _uiState.value = TrainerUiState.QuestionState(
                            session = session,
                            currentQuestion = firstQuestion,
                            questionNumber = 1,
                            totalQuestions = session.totalQuestions,
                            progress = 0f,
                            selectedOptionIndex = null,
                            feedback = null,
                            userStats = userStatsFlow.value,
                            isSoundEnabled = soundFeedbackManager.isSoundEnabled()
                        )
                    } else {
                        _uiState.value = TrainerUiState.Error("Nessuna domanda generata per questo argomento.")
                    }

                }
                is NetworkResult.Offline -> {
                    _uiState.value = TrainerUiState.Error(
                        message = "Connessione di rete assente. Usa una lezione offline.",
                        canRetry = true
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = TrainerUiState.Error(
                        message = result.message,
                        canRetry = true
                    )
                }
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        soundFeedbackManager.setSoundEnabled(enabled)
        val state = _uiState.value
        if (state is TrainerUiState.QuestionState) {
            _uiState.value = state.copy(isSoundEnabled = enabled)
        }
    }

    private fun handleSelectOption(index: Int) {
        val currentState = _uiState.value as? TrainerUiState.QuestionState ?: return
        // Prevent changing selection after answer has been checked
        if (currentState.isAnswerChecked) return

        _uiState.value = currentState.copy(selectedOptionIndex = index)
    }

    private fun handleCheckAnswer() {
        val currentState = _uiState.value as? TrainerUiState.QuestionState ?: return
        val selectedIndex = currentState.selectedOptionIndex ?: return
        if (currentState.isAnswerChecked) return

        val evaluation = submitAnswerUseCase(currentState.session, selectedIndex)

        // Trigger Audio & Haptic Feedback immediately
        if (evaluation.isCorrect) {
            soundFeedbackManager.playCorrectFeedback()
        } else {
            soundFeedbackManager.playIncorrectFeedback()
        }

        _uiState.value = currentState.copy(
            session = evaluation.updatedSession,
            feedback = AnswerFeedback(
                isCorrect = evaluation.isCorrect,
                selectedOptionIndex = selectedIndex,
                correctOptionIndex = evaluation.correctOptionIndex,
                correctAnswerText = evaluation.correctAnswerText,
                explanation = evaluation.explanation
            )
        )
    }

    private fun handleNextQuestion() {
        val currentState = _uiState.value as? TrainerUiState.QuestionState ?: return
        val currentSession = currentState.session
        val nextIndex = currentSession.currentQuestionIndex + 1

        if (nextIndex < currentSession.totalQuestions) {
            // Advance to next micro-question
            val updatedSession = currentSession.copy(currentQuestionIndex = nextIndex)
            val nextQuestion = updatedSession.currentQuestion ?: return

            _uiState.value = TrainerUiState.QuestionState(
                session = updatedSession,
                currentQuestion = nextQuestion,
                questionNumber = nextIndex + 1,
                totalQuestions = updatedSession.totalQuestions,
                progress = nextIndex.toFloat() / updatedSession.totalQuestions,
                selectedOptionIndex = null,
                feedback = null,
                userStats = userStatsFlow.value,
                isSoundEnabled = soundFeedbackManager.isSoundEnabled()
            )
        } else {
            // Completed all questions in the session!
            finishLesson(currentSession)
        }
    }

    private fun finishLesson(session: LessonSession) {
        viewModelScope.launch {
            // Save to Room database and update streak/XP
            completeLessonUseCase(session)

            soundFeedbackManager.playLessonCompleteFeedback()

            _uiState.value = TrainerUiState.CompleteState(
                session = session,
                score = session.score,
                totalQuestions = session.totalQuestions,
                accuracy = session.accuracyPercentage,
                xpEarned = session.xpEarned,
                userStats = userStatsFlow.value,
                topicTitle = session.topicTitle,
                topicDescription = session.topicDescription,
                topicExtract = session.topicExtract,
                thumbnailUrl = session.thumbnailUrl,
                wikiUrl = session.wikiUrl
            )
        }
    }

    private fun handleToggleSound() {
        val current = soundFeedbackManager.isSoundEnabled()
        soundFeedbackManager.setSoundEnabled(!current)
        val state = _uiState.value
        if (state is TrainerUiState.QuestionState) {
            _uiState.value = state.copy(isSoundEnabled = !current)
        }
    }
}
