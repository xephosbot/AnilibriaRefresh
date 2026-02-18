package com.xbot.localization

import com.xbot.domain.models.DomainError
import com.xbot.resources.Res
import com.xbot.resources.error_connection
import com.xbot.resources.error_http
import com.xbot.resources.error_serialization
import com.xbot.resources.error_unknown

fun Throwable.localizedMessage(): UiText {
    return when (this) {
        is DomainError.HttpError -> UiText.Text(Res.string.error_http, this.code)
        is DomainError.ConnectionError -> UiText.Text(Res.string.error_connection)
        is DomainError.SerializationError -> {
            val message = findRecursiveMessage(this) ?: "Empty message"
            UiText.Text(Res.string.error_serialization, message)
        }
        else -> {
            val message = findRecursiveMessage(this) ?: "Empty message"
            val rootCause = generateSequence(this) { it.cause }.last()

            val errorName = this::class.simpleName ?: "Unknown"
            val causeName = rootCause::class.simpleName ?: "Unknown"

            UiText.Text(Res.string.error_unknown, errorName, causeName, message)
        }
    }
}

private fun findRecursiveMessage(throwable: Throwable): String? {
    var current: Throwable? = throwable
    while (current != null) {
        val message = current.message
        if (!message.isNullOrBlank()) {
            return message
        }
        current = current.cause
    }
    return null
}
