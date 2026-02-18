package com.xbot.network.client

sealed class NetworkError(override val message: String? = null, override val cause: Throwable? = null) : Exception(message, cause) {
    data class HttpError(val code: Int, override val message: String?) : NetworkError(message)
    data class SerializationError(override val cause: Throwable) : NetworkError(cause.message, cause)
    data class ConnectionError(override val cause: Throwable) : NetworkError(cause.message, cause)
    data class UnknownError(override val cause: Throwable) : NetworkError(cause.message, cause)
}
