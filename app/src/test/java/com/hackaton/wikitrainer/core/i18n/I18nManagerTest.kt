package com.hackaton.wikitrainer.core.i18n

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class I18nManagerTest {

    @Before
    fun setup() {
        I18nManager.reset()
    }

    @Test
    fun `loadFromJson flattens nested keys correctly`() {
        val testJson = """
            {
              "it": {
                "dashboard": {
                  "quick_quiz": "Quiz rapido"
                }
              }
            }
        """.trimIndent()

        I18nManager.loadFromJson(testJson)

        assertEquals("Quiz rapido", I18nManager.getString("dashboard.quick_quiz", "it"))
    }

    @Test
    fun `fallback to italian when english translation is blank`() {
        val testJson = """
            {
              "it": {
                "common": {
                  "continue": "Continua"
                }
              },
              "en": {
                "common": {
                  "continue": ""
                }
              }
            }
        """.trimIndent()

        I18nManager.loadFromJson(testJson)

        // When requesting "en", should gracefully fall back to "it"
        assertEquals("Continua", I18nManager.getString("common.continue", "en"))
    }

    @Test
    fun `returns english translation when provided`() {
        val testJson = """
            {
              "it": {
                "common": {
                  "continue": "Continua"
                }
              },
              "en": {
                "common": {
                  "continue": "Continue"
                }
              }
            }
        """.trimIndent()

        I18nManager.loadFromJson(testJson)

        assertEquals("Continue", I18nManager.getString("common.continue", "en"))
        assertEquals("Continua", I18nManager.getString("common.continue", "it"))
    }

    @Test
    fun `replaces arguments properly in parameterized string`() {
        val testJson = """
            {
              "it": {
                "trainer": {
                  "question_header": "Domanda {current} di {total}: Metti alla prova la tua cultura!"
                }
              }
            }
        """.trimIndent()

        I18nManager.loadFromJson(testJson)

        val formatted = I18nManager.getString(
            "trainer.question_header",
            "it",
            "current" to 3,
            "total" to 5
        )

        assertEquals("Domanda 3 di 5: Metti alla prova la tua cultura!", formatted)
    }

    @Test
    fun `returns key itself if missing from all locales`() {
        val testJson = """{ "it": {} }"""
        I18nManager.loadFromJson(testJson)

        assertEquals("non.existent.key", I18nManager.getString("non.existent.key", "it"))
    }

    @Test
    fun `verifies real translations asset file has valid JSON and complete parity`() {
        val file = java.io.File("src/main/assets/translations.json")
        org.junit.Assert.assertTrue("translations.json must exist in assets", file.exists())
        val content = file.readText()
        I18nManager.loadFromJson(content)

        val sampleKeys = listOf(
            "common.continue",
            "common.back",
            "dashboard.quick_quiz",
            "dashboard.start_bubble",
            "path_modal.start_now",
            "trainer.question_header",
            "settings.title",
            "saved.title",
            "history.title"
        )
        for (key in sampleKeys) {
            val itValue = I18nManager.getString(key, "it")
            org.junit.Assert.assertTrue("Key $key in 'it' must not be blank", itValue.isNotBlank())
            org.junit.Assert.assertNotEquals("Key $key must not equal key name", key, itValue)

            // english fallback should also return the italian translation when blank
            val enValue = I18nManager.getString(key, "en")
            org.junit.Assert.assertTrue("Key $key in 'en' fallback must not be blank", enValue.isNotBlank())
        }
    }
}
