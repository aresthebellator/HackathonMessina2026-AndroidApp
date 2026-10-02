package com.exertia.wikingo.domain.usecase

import com.exertia.wikingo.core.network.NetworkResult
import com.exertia.wikingo.domain.model.LessonSession
import com.exertia.wikingo.domain.repository.LessonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLessonForTopicUseCaseTest {

    private val repository = mockk<LessonRepository>()
    private lateinit var useCase: GetLessonForTopicUseCase

    @Before
    fun setUp() {
        useCase = GetLessonForTopicUseCase(repository)
    }

    private fun createDummySession(topic: String): LessonSession {
        return LessonSession(
            id = "test-id",
            topicTitle = topic,
            topicDescription = "Desc",
            topicExtract = "Extract",
            thumbnailUrl = null,
            wikiUrl = "https://it.wikipedia.org/wiki/$topic",
            pageId = 1L,
            questions = emptyList()
        )
    }

    @Test
    fun `when topic lesson succeeds, it returns the topic session`() = runTest {
        val topicSession = createDummySession("Antico Egitto")
        coEvery { repository.getLessonForTopic("Antico Egitto", "it") } returns NetworkResult.Success(topicSession)

        val result = useCase("Antico Egitto", "it")

        assertTrue(result is NetworkResult.Success)
        assertEquals("Antico Egitto", (result as NetworkResult.Success).data.topicTitle)
        coVerify(exactly = 0) { repository.getRandomLesson(any()) }
    }

    @Test
    fun `when topic lesson fails with error, it falls back to getRandomLesson`() = runTest {
        val fallbackSession = createDummySession("Articolo Casuale")
        coEvery { repository.getLessonForTopic("ArgomentoInesistente", "it") } returns NetworkResult.Error(Exception("404"), "Not found")
        coEvery { repository.getRandomLesson("it") } returns NetworkResult.Success(fallbackSession)

        val result = useCase("ArgomentoInesistente", "it")

        assertTrue(result is NetworkResult.Success)
        assertEquals("Articolo Casuale", (result as NetworkResult.Success).data.topicTitle)
        coVerify(exactly = 1) { repository.getRandomLesson("it") }
    }
}
