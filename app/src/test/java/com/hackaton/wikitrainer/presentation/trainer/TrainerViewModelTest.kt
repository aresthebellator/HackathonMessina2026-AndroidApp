package com.hackaton.wikitrainer.presentation.trainer

import com.hackaton.wikitrainer.core.audio.SoundFeedbackManager
import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.Question
import com.hackaton.wikitrainer.domain.model.QuestionType
import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.repository.LessonRepository
import com.hackaton.wikitrainer.domain.usecase.CompleteLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetRandomLessonUseCase
import com.hackaton.wikitrainer.domain.usecase.GetUserStatsUseCase
import com.hackaton.wikitrainer.domain.usecase.SubmitAnswerUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrainerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<LessonRepository>()
    private val soundFeedbackManager = mockk<SoundFeedbackManager>(relaxed = true)

    private lateinit var getRandomLessonUseCase: GetRandomLessonUseCase
    private lateinit var submitAnswerUseCase: SubmitAnswerUseCase
    private lateinit var completeLessonUseCase: CompleteLessonUseCase
    private lateinit var getUserStatsUseCase: GetUserStatsUseCase

    private fun createSampleSession(): LessonSession {
        val q1 = Question(
            id = "q1",
            text = "Qual è la capitale d'Italia?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = listOf("Milano", "Roma", "Napoli", "Torino"),
            correctOptionIndex = 1,
            explanation = "Roma è la capitale d'Italia."
        )
        val q2 = Question(
            id = "q2",
            text = "Vero o Falso: Roma fu fondata nel 753 a.C.",
            type = QuestionType.TRUE_FALSE,
            options = listOf("Vero", "Falso"),
            correctOptionIndex = 0,
            explanation = "Fondata nel 753 a.C."
        )
        return LessonSession(
            id = "session-1",
            topicTitle = "Roma",
            topicDescription = "Capitale",
            topicExtract = "Roma è la capitale...",
            thumbnailUrl = null,
            wikiUrl = "https://it.wikipedia.org/wiki/Roma",
            pageId = 100,
            questions = listOf(q1, q2),
            currentQuestionIndex = 0,
            score = 0
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getRandomLessonUseCase = GetRandomLessonUseCase(repository)
        submitAnswerUseCase = SubmitAnswerUseCase()
        completeLessonUseCase = CompleteLessonUseCase(repository)
        getUserStatsUseCase = GetUserStatsUseCase(repository)

        every { repository.getUserStats() } returns flowOf(
            UserStats(currentStreak = 2, bestStreak = 5, totalXp = 150, totalLessonsCompleted = 10, lastActiveDate = "2026-09-17")
        )
        coEvery { repository.getRandomLesson(any()) } returns NetworkResult.Success(createSampleSession())
        coEvery { repository.saveCompletedLesson(any()) } returns 1L
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads random lesson and enters QuestionState`() = runTest {
        val viewModel = TrainerViewModel(
            getRandomLessonUseCase,
            submitAnswerUseCase,
            completeLessonUseCase,
            getUserStatsUseCase,
            soundFeedbackManager
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be QuestionState but was $state", state is TrainerUiState.QuestionState)
        val questionState = state as TrainerUiState.QuestionState
        assertEquals(1, questionState.questionNumber)
        assertEquals(2, questionState.totalQuestions)
        assertEquals("Roma", questionState.session.topicTitle)
        assertEquals(10, questionState.hearts)
    }

    @Test
    fun `selecting option and checking answer updates feedback and triggers sound`() = runTest {
        val viewModel = TrainerViewModel(
            getRandomLessonUseCase,
            submitAnswerUseCase,
            completeLessonUseCase,
            getUserStatsUseCase,
            soundFeedbackManager
        )
        advanceUntilIdle()

        viewModel.onEvent(TrainerUiEvent.SelectOption(1)) // Select Roma (correct)
        viewModel.onEvent(TrainerUiEvent.CheckAnswer)

        val state = viewModel.uiState.value as TrainerUiState.QuestionState
        assertNotNull(state.feedback)
        assertTrue(state.feedback!!.isCorrect)
        assertEquals(1, state.session.score)

        // Verify correct sound played
        io.mockk.verify { soundFeedbackManager.playCorrectFeedback() }
    }

    @Test
    fun `wrong answer decrements a heart`() = runTest {
        val viewModel = TrainerViewModel(
            getRandomLessonUseCase,
            submitAnswerUseCase,
            completeLessonUseCase,
            getUserStatsUseCase,
            soundFeedbackManager
        )
        advanceUntilIdle()

        viewModel.onEvent(TrainerUiEvent.SelectOption(0))
        viewModel.onEvent(TrainerUiEvent.CheckAnswer)

        val state = viewModel.uiState.value as TrainerUiState.QuestionState
        assertEquals(9, state.hearts)
        assertTrue(!state.feedback!!.isCorrect)
    }

    @Test
    fun `completing all questions transitions to CompleteState and saves lesson`() = runTest {
        val viewModel = TrainerViewModel(
            getRandomLessonUseCase,
            submitAnswerUseCase,
            completeLessonUseCase,
            getUserStatsUseCase,
            soundFeedbackManager
        )
        advanceUntilIdle()

        // Answer Q1
        viewModel.onEvent(TrainerUiEvent.SelectOption(1))
        viewModel.onEvent(TrainerUiEvent.CheckAnswer)
        viewModel.onEvent(TrainerUiEvent.NextQuestion)

        // Answer Q2
        viewModel.onEvent(TrainerUiEvent.SelectOption(0))
        viewModel.onEvent(TrainerUiEvent.CheckAnswer)
        viewModel.onEvent(TrainerUiEvent.NextQuestion)

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue("Final state should be CompleteState but was $finalState", finalState is TrainerUiState.CompleteState)
        val completeState = finalState as TrainerUiState.CompleteState
        assertEquals(2, completeState.score)
        assertEquals(100, completeState.accuracy)

        coVerify { repository.saveCompletedLesson(any()) }
        io.mockk.verify { soundFeedbackManager.playLessonCompleteFeedback() }
    }
}
