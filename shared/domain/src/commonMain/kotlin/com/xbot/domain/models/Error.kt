package com.xbot.domain.models

sealed class Error {
    data class HttpError(val code: Int, val message: String?) : Error()
    data class SerializationError(val cause: Throwable) : Error()
    data class NetworkException(val cause: Throwable) : Error()
}