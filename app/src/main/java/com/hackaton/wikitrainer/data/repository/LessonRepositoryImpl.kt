package com.hackaton.wikitrainer.data.repository

import com.hackaton.wikitrainer.core.network.NetworkResult
import com.hackaton.wikitrainer.data.generator.QuestionGenerator
import com.hackaton.wikitrainer.data.local.dao.TopicHistoryDao
import com.hackaton.wikitrainer.data.local.dao.UserStreakDao
import com.hackaton.wikitrainer.data.local.entity.TopicHistoryEntity
import com.hackaton.wikitrainer.data.local.entity.UserStreakEntity
import com.hackaton.wikitrainer.data.remote.WikipediaClient
import com.hackaton.wikitrainer.domain.model.LessonSession
import com.hackaton.wikitrainer.domain.model.TopicHistory
import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

class LessonRepositoryImpl(
    private val wikipediaClient: WikipediaClient,
    private val questionGenerator: QuestionGenerator,
    private val topicHistoryDao: TopicHistoryDao,
    private val userStreakDao: UserStreakDao
) : LessonRepository {

    override suspend fun getRandomLesson(language: String): NetworkResult<LessonSession> {
        val result = wikipediaClient.fetchRandomArticle(language)

        return when (result) {
            is NetworkResult.Success -> {
                val summary = result.data
                val questions = questionGenerator.generateQuestions(summary)
                val wikiUrl = summary.contentUrls?.mobile?.page
                    ?: summary.contentUrls?.desktop?.page
                    ?: "https://$language.wikipedia.org/wiki/${summary.title}"

                val session = LessonSession(
                    id = UUID.randomUUID().toString(),
                    topicTitle = summary.title,
                    topicDescription = summary.description ?: "Argomento di cultura generale",
                    topicExtract = summary.extract ?: "",
                    thumbnailUrl = summary.thumbnail?.source ?: summary.originalImage?.source,
                    wikiUrl = wikiUrl,
                    pageId = summary.pageId ?: System.currentTimeMillis(),
                    questions = questions
                )
                NetworkResult.Success(session)
            }
            is NetworkResult.Offline -> {
                // Guaranteed offline learning fallback
                val fallbackSummary = questionGenerator.getCuratedOfflineSummary()
                val questions = questionGenerator.generateQuestions(fallbackSummary)
                val session = LessonSession(
                    id = UUID.randomUUID().toString(),
                    topicTitle = "${fallbackSummary.title} (Offline)",
                    topicDescription = fallbackSummary.description ?: "Lezione salvata offline",
                    topicExtract = fallbackSummary.extract ?: "",
                    thumbnailUrl = null,
                    wikiUrl = "https://$language.wikipedia.org/wiki/${fallbackSummary.title}",
                    pageId = fallbackSummary.pageId ?: 0L,
                    questions = questions
                )
                NetworkResult.Success(session)
            }
            is NetworkResult.Error -> {
                result
            }
        }
    }

    override suspend fun getLessonForTopic(topic: String, language: String): NetworkResult<LessonSession> {
        val result = wikipediaClient.fetchArticleByTitle(topic, language)
        return when (result) {
            is NetworkResult.Success -> {
                val summary = result.data
                val questions = questionGenerator.generateQuestions(summary)
                val wikiUrl = summary.contentUrls?.mobile?.page
                    ?: "https://$language.wikipedia.org/wiki/${summary.title}"

                val session = LessonSession(
                    id = UUID.randomUUID().toString(),
                    topicTitle = summary.title,
                    topicDescription = summary.description ?: "Argomento selezionato",
                    topicExtract = summary.extract ?: "",
                    thumbnailUrl = summary.thumbnail?.source,
                    wikiUrl = wikiUrl,
                    pageId = summary.pageId ?: System.currentTimeMillis(),
                    questions = questions
                )
                NetworkResult.Success(session)
            }
            is NetworkResult.Offline -> {
                getRandomLesson(language)
            }
            is NetworkResult.Error -> result
        }
    }

    override suspend fun saveCompletedLesson(session: LessonSession): Long {
        val entity = TopicHistoryEntity(
            pageId = session.pageId,
            title = session.topicTitle,
            description = session.topicDescription,
            extract = session.topicExtract,
            thumbnailUrl = session.thumbnailUrl,
            wikiUrl = session.wikiUrl,
            score = session.score,
            totalQuestions = session.totalQuestions,
            completedAt = System.currentTimeMillis(),
            xpEarned = session.xpEarned
        )
        val savedId = topicHistoryDao.insertTopic(entity)

        // Streak and XP calculation
        updateStreakAndXp(session.xpEarned)

        return savedId
    }

    private suspend fun updateStreakAndXp(xpEarned: Int) {
        val today = LocalDate.now()
        val todayString = today.toString()
        val currentStreakEntity = userStreakDao.getStreakSync()

        if (currentStreakEntity == null) {
            val initial = UserStreakEntity(
                id = 1,
                currentStreak = 1,
                bestStreak = 1,
                totalXp = xpEarned,
                totalLessonsCompleted = 1,
                lastActiveDate = todayString
            )
            userStreakDao.saveStreak(initial)
            return
        }

        val lastDateStr = currentStreakEntity.lastActiveDate
        val newStreak: Int
        if (lastDateStr.isBlank()) {
            newStreak = 1
        } else {
            val lastDate = runCatching { LocalDate.parse(lastDateStr) }.getOrNull()
            if (lastDate == null) {
                newStreak = 1
            } else {
                val daysDiff = ChronoUnit.DAYS.between(lastDate, today)
                newStreak = when {
                    daysDiff == 0L -> currentStreakEntity.currentStreak // Already active today
                    daysDiff == 1L -> currentStreakEntity.currentStreak + 1 // Streak preserved!
                    else -> 1 // Streak lost, start again
                }
            }
        }

        val updatedBest = maxOf(currentStreakEntity.bestStreak, newStreak)
        val updatedXp = currentStreakEntity.totalXp + xpEarned
        val updatedLessons = currentStreakEntity.totalLessonsCompleted + 1

        val updatedEntity = currentStreakEntity.copy(
            currentStreak = newStreak,
            bestStreak = updatedBest,
            totalXp = updatedXp,
            totalLessonsCompleted = updatedLessons,
            lastActiveDate = todayString
        )
        userStreakDao.saveStreak(updatedEntity)
    }

    override fun getTopicHistory(): Flow<List<TopicHistory>> {
        return topicHistoryDao.getAllTopics().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getUserStats(): Flow<UserStats> {
        return userStreakDao.getStreak().map { entity ->
            entity?.toDomain() ?: UserStats(
                currentStreak = 0,
                bestStreak = 0,
                totalXp = 0,
                totalLessonsCompleted = 0,
                lastActiveDate = ""
            )
        }
    }

    override suspend fun getTopicById(id: Long): TopicHistory? {
        return topicHistoryDao.getTopicById(id)?.toDomain()
    }
}
