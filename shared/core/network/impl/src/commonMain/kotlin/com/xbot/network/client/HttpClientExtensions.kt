package com.xbot.network.client

import arrow.core.raise.Raise
import com.xbot.common.error.AppError
import com.xbot.network.utils.toAppError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey
import kotlinx.coroutines.CancellationException

context(raise: Raise<AppError>)
internal suspend inline fun <reified T> HttpClient.request(
    block: suspend HttpClient.() -> HttpResponse
): T = try {
    block().body<T>()
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    raise.raise(e.toAppError())
}

internal val AuthenticatedRequest = AttributeKey<Unit>("AuthenticatedRequest")

internal fun HttpRequestBuilder.requiresAuth() {
    attributes.put(AuthenticatedRequest, Unit)
}
