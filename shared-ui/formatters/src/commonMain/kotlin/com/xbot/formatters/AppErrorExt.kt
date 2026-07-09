package com.xbot.formatters

import com.xbot.common.error.AppError
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.error_connection
import com.xbot.resources.error_http
import com.xbot.resources.error_unknown

fun Throwable.localizedMessage(): StringResource {
    return when (this) {
        is AppError.ServerError -> {
            val message = this.message
            if (!message.isNullOrBlank()) {
                StringResource.String(message)
            } else {
                StringResource.Text(Res.string.error_http, this.code)
            }
        }
        is AppError.ConnectionError -> StringResource.Text(Res.string.error_connection)
        else -> {
            val message = findRecursiveMessage(this) ?: "Empty message"
            val rootCause = generateSequence(this) { it.cause }.last()

            val errorName = this::class.simpleName ?: "Unknown"
            val causeName = rootCause::class.simpleName ?: "Unknown"

            StringResource.Text(Res.string.error_unknown, errorName, causeName, message)
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
