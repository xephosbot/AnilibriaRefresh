package com.xbot.network.client

sealed interface NetworkError {
    data class HttpError(val code: Int, val message: String?) : NetworkError
    data class SerializationError(val cause: Throwable) : NetworkError
    data class ConnectionError(val cause: Throwable) : NetworkError
    data class UnknownError(val cause: Throwable) : NetworkError
}