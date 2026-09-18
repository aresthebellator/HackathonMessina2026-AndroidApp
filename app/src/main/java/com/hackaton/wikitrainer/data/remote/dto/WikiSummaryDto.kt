package com.hackaton.wikitrainer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiSummaryDto(
    @SerialName("title") val title: String = "",
    @SerialName("displaytitle") val displayTitle: String? = null,
    @SerialName("pageid") val pageId: Long? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("extract") val extract: String? = null,
    @SerialName("thumbnail") val thumbnail: WikiThumbnailDto? = null,
    @SerialName("originalimage") val originalImage: WikiThumbnailDto? = null,
    @SerialName("content_urls") val contentUrls: WikiContentUrlsDto? = null,
    @SerialName("lang") val lang: String? = null
)

@Serializable
data class WikiThumbnailDto(
    @SerialName("source") val source: String,
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0
)

@Serializable
data class WikiContentUrlsDto(
    @SerialName("desktop") val desktop: WikiPageUrlDto? = null,
    @SerialName("mobile") val mobile: WikiPageUrlDto? = null
)

@Serializable
data class WikiPageUrlDto(
    @SerialName("page") val page: String = ""
)
