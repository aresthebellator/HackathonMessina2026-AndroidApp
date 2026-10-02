package com.exertia.wikingo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exertia.wikingo.domain.model.UserStats

@Entity(tableName = "user_streak")
data class UserStreakEntity(
    @PrimaryKey
    val id: Int = 1,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalXp: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val lastActiveDate: String = ""
) {
    fun toDomain(): UserStats = UserStats(
        currentStreak = currentStreak,
        bestStreak = bestStreak,
        totalXp = totalXp,
        totalLessonsCompleted = totalLessonsCompleted,
        lastActiveDate = lastActiveDate
    )
}
