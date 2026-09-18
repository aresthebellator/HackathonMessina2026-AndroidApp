package com.hackaton.wikitrainer.domain.model

data class UserStats(
    val currentStreak: Int,
    val bestStreak: Int,
    val totalXp: Int,
    val totalLessonsCompleted: Int,
    val lastActiveDate: String
)
