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
    val textColor: Color,
    val englishTitle: String = title,
    val englishSubtitle: String = subtitle,
    val englishTopic: String = topic,
    val englishDescription: String = description,
    val englishKeywords: List<String> = keywords
) {
    fun localizedTitle(language: String) = if (language == "en") englishTitle else title
    fun localizedSubtitle(language: String) = if (language == "en") englishSubtitle else subtitle
    fun localizedTopic(language: String) = if (language == "en") englishTopic else topic
    fun localizedDescription(language: String) = if (language == "en") englishDescription else description
    fun localizedKeywords(language: String) = if (language == "en") englishKeywords else keywords
}
