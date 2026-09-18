package com.hackaton.wikitrainer.data.local

import android.content.Context
import com.hackaton.wikitrainer.domain.model.TopicHistory
import org.json.JSONArray
import org.json.JSONObject

data class SavedArticle(
    val pageId: Long,
    val title: String,
    val description: String,
    val extract: String,
    val thumbnailUrl: String?,
    val wikiUrl: String
)

class SavedArticleStore(context: Context) {
    private val preferences = context.getSharedPreferences("saved_articles", Context.MODE_PRIVATE)

    fun getAll(): List<SavedArticle> {
        val raw = preferences.getString(KEY_ITEMS, "[]") ?: "[]"
        val json = JSONArray(raw)
        return buildList {
            for (index in 0 until json.length()) {
                val item = json.getJSONObject(index)
                add(
                    SavedArticle(
                        pageId = item.getLong("pageId"),
                        title = item.getString("title"),
                        description = item.optString("description"),
                        extract = item.optString("extract"),
                        thumbnailUrl = item.optString("thumbnailUrl").ifBlank { null },
                        wikiUrl = item.getString("wikiUrl")
                    )
                )
            }
        }
    }

    fun toggle(article: SavedArticle) {
        val updated = getAll().toMutableList()
        val existing = updated.indexOfFirst { it.pageId == article.pageId }
        if (existing >= 0) updated.removeAt(existing) else updated.add(0, article)
        save(updated)
    }

    fun remove(pageId: Long) {
        save(getAll().filterNot { it.pageId == pageId })
    }

    private fun save(items: List<SavedArticle>) {
        val json = JSONArray()
        items.forEach { article ->
            json.put(
                JSONObject().apply {
                    put("pageId", article.pageId)
                    put("title", article.title)
                    put("description", article.description)
                    put("extract", article.extract)
                    put("thumbnailUrl", article.thumbnailUrl ?: "")
                    put("wikiUrl", article.wikiUrl)
                }
            )
        }
        preferences.edit().putString(KEY_ITEMS, json.toString()).apply()
    }

    companion object {
        private const val KEY_ITEMS = "items"
    }
}
