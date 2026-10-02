package com.exertia.wikingo.data.remote

import com.exertia.wikingo.core.network.ConnectivityObserver
import com.exertia.wikingo.core.network.NetworkResult
import com.exertia.wikingo.data.remote.dto.WikiSummaryDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.net.Uri

/**
 * Service Client for Wikipedia REST API:
 * Handles network requests, retries with exponential backoff,
 * offline detection, disambiguation page filtration, and robust error mapping.
 */
class WikipediaClient(
    private val apiService: WikipediaApiService,
    private val connectivityObserver: ConnectivityObserver,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Fetches a random article summary from Wikipedia.
     * Retries automatically if the returned article is a disambiguation page or has no substantial extract.
     */
    suspend fun fetchRandomArticle(
        language: String = "it",
        maxRetries: Int = 3
    ): NetworkResult<WikiSummaryDto> = withContext(ioDispatcher) {
        if (!connectivityObserver.isConnected()) {
            return@withContext NetworkResult.Offline("Nessuna connessione internet attiva. Modalità offline.")
        }

        var attempts = 0
        var lastException: Throwable? = null

        while (attempts < maxRetries) {
            attempts++
            val url = "https://$language.wikipedia.org/api/rest_v1/page/random/summary"
            try {
                val summary = apiService.getSummaryFromUrl(url)

                // Validate article content: must not be a stub or disambiguation
                val title = summary.title
                val extract = summary.extract ?: ""
                val isDisambiguation = title.contains("(disambigua)", ignoreCase = true) ||
                        summary.description?.contains("disam", ignoreCase = true) == true

                if (isDisambiguation || extract.length < 80) {
                    // Try fetching another random page
                    delay(300)
                    continue
                }

                return@withContext NetworkResult.Success(summary)
            } catch (e: HttpException) {
                lastException = e
                if (e.code() == 429) {
                    // Rate limit: back off
                    delay(1000L * attempts)
                } else if (e.code() in 500..599) {
                    delay(500L * attempts)
                } else {
                    return@withContext NetworkResult.Error(
                        exception = e,
                        message = "Errore server Wikipedia (${e.code()}): ${e.message()}"
                    )
                }
            } catch (e: UnknownHostException) {
                return@withContext NetworkResult.Offline("Impossibile raggiungere Wikipedia. Verifica la connessione di rete.")
            } catch (e: SocketTimeoutException) {
                lastException = e
                delay(500L * attempts)
            } catch (e: IOException) {
                lastException = e
                delay(500L * attempts)
            } catch (e: Exception) {
                return@withContext NetworkResult.Error(
                    exception = e,
                    message = e.localizedMessage ?: "Errore imprevisto durante il recupero da Wikipedia"
                )
            }
        }

        NetworkResult.Error(
            exception = lastException ?: IOException("Tentativi esauriti senza successo."),
            message = "Impossibile recuperare un articolo valido da Wikipedia dopo vari tentativi."
        )
    }

    /**
     * Fetches a specific article summary by its title (useful for curated or search-based lessons).
     */
    suspend fun fetchArticleByTitle(
        title: String,
        language: String = "it"
    ): NetworkResult<WikiSummaryDto> = withContext(ioDispatcher) {
        if (!connectivityObserver.isConnected()) {
            return@withContext NetworkResult.Offline("Nessuna connessione internet attiva.")
        }

        try {
            val url = "https://$language.wikipedia.org/api/rest_v1/page/summary/${Uri.encode(title)}"
            val summary = apiService.getSummaryFromUrl(url)
            NetworkResult.Success(summary)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                404 -> "Articolo '$title' non trovato su Wikipedia."
                429 -> "Troppe richieste verso Wikipedia. Riprova tra poco."
                else -> "Errore di rete Wikipedia (${e.code()})."
            }
            NetworkResult.Error(e, message)
        } catch (e: UnknownHostException) {
            NetworkResult.Offline("Impossibile connettersi a Wikipedia. Sei offline.")
        } catch (e: IOException) {
            NetworkResult.Error(e, "Errore di comunicazione con Wikipedia: ${e.localizedMessage}")
        } catch (e: Exception) {
            NetworkResult.Error(e, e.localizedMessage ?: "Errore imprevisto.")
        }
    }
}
