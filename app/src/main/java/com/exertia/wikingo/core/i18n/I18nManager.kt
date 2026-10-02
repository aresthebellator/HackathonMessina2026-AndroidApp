package com.exertia.wikingo.core.i18n

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Manages runtime UI string translations loaded from translations.json.
 * Supports hierarchical JSON structure (e.g. dashboard.quick_quiz),
 * automatic fallback to Italian ("it") when a key is blank or missing in the active locale,
 * and variable replacement (e.g. {current}, {total}, {xp}).
 */
object I18nManager {
    private var translationsMap: Map<String, Map<String, String>> = emptyMap()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    fun isLoaded(): Boolean = translationsMap.isNotEmpty()

    fun load(context: Context) {
        if (isLoaded()) return
        try {
            val jsonString = context.assets.open("translations.json").bufferedReader().use { it.readText() }
            loadFromJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFromJson(jsonString: String) {
        val root = jsonParser.parseToJsonElement(jsonString) as? JsonObject ?: return
        val parsed = mutableMapOf<String, MutableMap<String, String>>()

        for ((lang, langElem) in root) {
            val langMap = mutableMapOf<String, String>()
            if (langElem is JsonObject) {
                flattenJson("", langElem, langMap)
            }
            parsed[lang] = langMap
        }
        translationsMap = parsed
    }

    private fun flattenJson(prefix: String, obj: JsonObject, target: MutableMap<String, String>) {
        for ((key, elem) in obj) {
            val currentKey = if (prefix.isEmpty()) key else "$prefix.$key"
            if (elem is JsonObject) {
                flattenJson(currentKey, elem, target)
            } else {
                val value = elem.jsonPrimitive.contentOrNull ?: ""
                target[currentKey] = value
            }
        }
    }

    fun getString(key: String, lang: String = "it", vararg args: Pair<String, Any>): String {
        val langMap = translationsMap[lang]
        val raw = langMap?.get(key)?.takeIf { it.isNotBlank() }
            ?: translationsMap["it"]?.get(key)?.takeIf { it.isNotBlank() }
            ?: key

        var result = raw
        for ((argKey, argVal) in args) {
            result = result.replace("{$argKey}", argVal.toString())
        }
        return result
    }

    /**
     * Resets the loaded translations (mainly for testing).
     */
    fun reset() {
        translationsMap = emptyMap()
    }
}

val LocalI18nLanguage = compositionLocalOf { "it" }

/**
 * Convenient Composable helper for getting localized strings using LocalI18nLanguage.
 */
@Composable
fun i18n(key: String, vararg args: Pair<String, Any>): String {
    val lang = LocalI18nLanguage.current
    return I18nManager.getString(key, lang, *args)
}
