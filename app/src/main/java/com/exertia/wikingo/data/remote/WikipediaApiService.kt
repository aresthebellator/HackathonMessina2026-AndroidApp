package com.exertia.wikingo.data.remote

import com.exertia.wikingo.data.remote.dto.WikiSummaryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WikipediaApiService {

    @GET("api/rest_v1/page/random/summary")
    suspend fun getRandomSummary(): WikiSummaryDto

    @GET("api/rest_v1/page/summary/{title}")
    suspend fun getSummaryByTitle(
        @Path("title") title: String
    ): WikiSummaryDto

    @GET
    suspend fun getSummaryFromUrl(
        @Url url: String
    ): WikiSummaryDto
}
