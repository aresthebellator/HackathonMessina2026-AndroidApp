package com.hackaton.wikitrainer.domain.model

import androidx.compose.ui.graphics.Color

data class PathUnit(
    val id: Int,
    val title: String,
    val subtitle: String,
    val topic: String,
    val description: String,
    val startLesson: Int,
    val endLesson: Int,
    val iconName: String,
    val keywords: List<String>,
    val primaryColor: Color,
    val darkColor: Color,
    val lightColor: Color,
    val textColor: Color
)
