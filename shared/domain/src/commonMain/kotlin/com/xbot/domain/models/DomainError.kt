package com.xbot.domain.models

sealed interface DomainError {
    data class HttpError(val code: Int, val message: String?) : DomainError

    data class SerializationError(val cause: Throwable) : DomainError

    data class ConnectionError(val cause: Throwable) : DomainError

    data class UnknownError(val cause: Throwable) : DomainError
}