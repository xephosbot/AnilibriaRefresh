package com.xbot.network.utils

import com.xbot.common.error.AppError
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.unwrapCancellationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

internal suspend fun Throwable.toAppError(): AppError = when {
    this.isNoConnectionException() || this.isTimeoutException() -> AppError.ConnectionError(this)
    this is ResponseException -> AppError.ServerError(response.status.value, response.parseServerMessage())
    else -> AppError.UnknownError(this)
}

internal fun Throwable.isTimeoutException(): Boolean {
    val exception = unwrapCancellationException()
    return exception is HttpRequestTimeoutException ||
            exception is ConnectTimeoutException ||
            exception is SocketTimeoutException
}

internal expect fun Throwable.isNoConnectionException(): Boolean

private suspend fun HttpResponse.parseServerMessage(): String? = runCatching {
    body<JsonObject>()["message"]?.jsonPrimitive?.contentOrNull
}.getOrNull()
