package com.exertia.wikingo.domain.model

data class Question(
    val id: String,
    val text: String,
    val type: QuestionType,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val wikiQuote: String = ""
) {
    val correctAnswerText: String
        get() = options.getOrElse(correctOptionIndex) { "" }

    fun isAnswerCorrect(selectedIndex: Int): Boolean = selectedIndex == correctOptionIndex
}
