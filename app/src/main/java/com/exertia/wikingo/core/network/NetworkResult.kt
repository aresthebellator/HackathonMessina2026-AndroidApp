package com.exertia.wikingo.core.network

/**
 * Sealed interface for type-safe network and operation results.
 */
sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val exception: Throwable, val message: String) : NetworkResult<Nothing>
    data class Offline(val message: String = "Sei offline. Utilizzo contenuti salvati.") : NetworkResult<Nothing>
}
