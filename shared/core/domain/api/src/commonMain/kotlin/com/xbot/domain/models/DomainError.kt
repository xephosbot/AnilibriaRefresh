package com.xbot.domain.models

sealed class DomainError(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    data class HttpError(val code: Int, override val message: String?) : DomainError(message)
    data class SerializationError(override val cause: Throwable) : DomainError(cause.message, cause)
    data class ConnectionError(override val cause: Throwable) : DomainError(cause.message, cause)
    data class Timeout(override val cause: Throwable?) : DomainError("Request timed out", cause)
    class NoConnection : DomainError("No network available")
    data class UnknownError(override val cause: Throwable) : DomainError(cause.message, cause)

    /**
     * Retry predicate used by ResilientHttpRequester's Schedule.
     * NoConnection is intentionally NOT retryable — it's gated *before* the request by
     * the connectivity pre-check; waiting for reconnection is a different policy
     * (observable via ConnectivityMonitor.observe()).
     */
    val isRetryable: Boolean
        get() = when (this) {
            is ConnectionError, is Timeout -> true
            is HttpError -> code in 500..599 || code == 429 || code == 408
            is SerializationError, is UnknownError, is NoConnection -> false
        }
}
