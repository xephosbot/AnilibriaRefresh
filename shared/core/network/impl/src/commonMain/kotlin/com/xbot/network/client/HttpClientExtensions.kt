package com.xbot.network.client

import arrow.core.raise.Raise
import com.xbot.domain.models.DomainError
import com.xbot.network.plugins.NoConnectionException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import io.ktor.util.AttributeKey
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

expect class UnknownHostException : Exception

/**
 * Executes [block] on the receiver [HttpClient] exactly once and maps every known
 * failure mode to the appropriate [DomainError] variant.
 *
 * This is the low-level primitive: it does NOT retry, does NOT do a connectivity
 * pre-check, and does NOT know about backoff. Those concerns live in
 * [ResilientHttpRequester], which calls this inside [arrow.resilience.Schedule].
 *
 * `CancellationException` is always rethrown so coroutine cancellation propagates
 * correctly through the retry loop above.
 */
context(raise: Raise<DomainError>)
internal suspend inline fun <reified T> HttpClient.singleAttempt(
    noinline block: suspend HttpClient.() -> HttpResponse,
): T = try {
    val response = block()
    response.body<T>()
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    raise.raise(e.toDomainError())
}

internal suspend fun Throwable.toDomainError(): DomainError = when (this) {
    is NoConnectionException -> DomainError.NoConnection()
    is HttpRequestTimeoutException -> DomainError.Timeout(this)
    is ResponseException -> DomainError.HttpError(response.status.value, response.bodyAsText())
    is UnknownHostException, is UnresolvedAddressException, is IOException -> DomainError.ConnectionError(this)
    is SerializationException, is JsonConvertException -> DomainError.SerializationError(this)
    else -> DomainError.UnknownError(this)
}

internal val AuthenticatedRequest = AttributeKey<Unit>("AuthenticatedRequest")

internal fun HttpRequestBuilder.requiresAuth() {
    attributes.put(AuthenticatedRequest, Unit)
}
