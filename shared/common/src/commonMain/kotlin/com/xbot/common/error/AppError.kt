package com.xbot.common.error

sealed class AppError(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception(message, cause) {

    /** HTTP error response from the server. [message] is the human-readable message parsed from the response body, if any. */
    data class ServerError(val code: Int, override val message: String?) : AppError(message)

    /** Any transport-level failure: timeout, DNS, no network, SSL handshake, socket I/O. */
    data class ConnectionError(override val cause: Throwable) : AppError(cause.message, cause)

    /** Unexpected failure (including malformed/unparseable payloads). Reported to crash analytics. */
    data class UnknownError(override val cause: Throwable) : AppError(cause.message, cause)
}
