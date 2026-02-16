package com.xbot.domain.models

sealed class DomainError(override val message: String? = null, override val cause: Throwable? = null) : Exception(message, cause) {
    data class HttpError(val code: Int, override val message: String?) : DomainError(message)
    data class SerializationError(override val cause: Throwable) : DomainError(cause.message, cause)
    data class ConnectionError(override val cause: Throwable) : DomainError(cause.message, cause)
    data class UnknownError(override val cause: Throwable) : DomainError(cause.message, cause)
}
