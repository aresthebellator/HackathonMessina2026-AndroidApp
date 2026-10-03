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

    /**
     * Long or underspecified questions benefit from opening the source article
     * directly, so the learner can inspect the surrounding context.
     */
    val shouldOfferDeepening: Boolean
        get() = text.length > 180 ||
            explanation.length > 220 ||
            wikiQuote.isBlank() ||
            text.contains("sconosciut", ignoreCase = true) ||
            text.contains("unknown", ignoreCase = true) ||
            text.contains("general knowledge", ignoreCase = true)

    fun isAnswerCorrect(selectedIndex: Int): Boolean = selectedIndex == correctOptionIndex
}
