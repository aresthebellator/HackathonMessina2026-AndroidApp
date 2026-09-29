package com.hackaton.wikitrainer.presentation.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.wikitrainer.core.audio.SoundFeedbackManager
import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.usecase.CompleteLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetLessonForTopicUseCase
import com.hackaton.wikitrainer.domain.usecase.GetRandomLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetUserStatsUseCase
import com.hackaton.wikitrainer.domain.usecase.HeartsManager
import com.hackaton.wikitrainer.domain.usecase.SubmitAnswerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class TrainerViewModel(
    private val getRandomLessonUseCase: GetRandomLessonUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase,
    getUserStatsUseCase: GetUserStatsUseCase,
    private val soundFeedbackManager: SoundFeedbackManager,
    private val getLessonForTopicUseCase: GetLessonForTopicUseCase? = null,
    private val heartsManager: HeartsManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrainerUiState>(TrainerUiState.Loading())
    val uiState: StateFlow<TrainerUiState> = _uiState.asStateFlow()

    private val userStatsFlow: StateFlow<UserStats> = getUserStatsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStats(0, 0, 0, 0, "")
        )
    private var lessonLoadJob: Job? = null
    private var lastLanguage = "it"
    private var lastTopic: String? = null
    private var lastLessonNumber: Int? = null

    init {
        loadNewLesson()
    }

    fun onEvent(event: TrainerUiEvent) {
        when (event) {
            is TrainerUiEvent.SelectOption -> handleSelectOption(event.index)
            is TrainerUiEvent.CheckAnswer -> handleCheckAnswer()
            is TrainerUiEvent.NextQuestion -> handleNextQuestion()
            is TrainerUiEvent.StartNewLesson -> loadNewLesson()
            is TrainerUiEvent.Retry -> loadLesson(lastLanguage, lastTopic, lastLessonNumber)
            is TrainerUiEvent.ToggleSound -> handleToggleSound()
        }
    }

    fun loadNewLesson(language: String = "it") {
        loadLesson(language = language)
    }

    fun loadLesson(language: String = "it", topic: String? = null, lessonNumber: Int? = null) {
        lessonLoadJob?.cancel()
        lastLanguage = language
        lastTopic = topic
        lastLessonNumber = lessonNumber
        lessonLoadJob = viewModelScope.launch {
            _uiState.value = TrainerUiState.Loading(
                if (topic != null) {
                    if (language == "en") "Loading lesson: $topic..." else "Carico la lezione: $topic..."
                } else {
                    if (language == "en") "Exploring Wikipedia for a new lesson..." else "Esploro Wikipedia per una nuova lezione..."
                }
            )

            val result = if (!topic.isNullOrBlank() && getLessonForTopicUseCase != null) {
                getLessonForTopicUseCase(topic, language)
            } else {
                getRandomLessonUseCase(language)
            }

            when (result) {
                is NetworkResult.Success -> {
                    val session = result.data
                    val firstQuestion = session.currentQuestion
                    if (firstQuestion != null) {
                        val hearts = heartsManager?.getState()?.count ?: 10
                        if (hearts == 0) {
                            _uiState.value = TrainerUiState.Error(
                                message = if (language == "en") {
                                    "You are out of hearts. Come back in 2 hours to recharge one."
                                } else {
                                    "Hai esaurito i cuori. Torna tra 2 ore per ricaricarne uno."
                                },
                                canRetry = true
                            )
                            return@launch
                        }
                        _uiState.value = TrainerUiState.QuestionState(
                            session = session,
                            currentQuestion = firstQuestion,
                            questionNumber = 1,
                            totalQuestions = session.totalQuestions,
                            progress = 0f,
                            selectedOptionIndex = null,
                            feedback = null,
                            userStats = userStatsFlow.value,
                            hearts = hearts,
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
        val hearts = if (evaluation.isCorrect) {
            currentState.hearts
        } else {
            heartsManager?.consumeHeart()?.count ?: (currentState.hearts - 1).coerceAtLeast(0)
        }

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
            ),
            hearts = hearts
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
                hearts = heartsManager?.getState()?.count ?: currentState.hearts,
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
