package com.xbot.network.client

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.xbot.domain.models.DomainError
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
internal suspend inline fun <reified T> HttpClient.singleAttempt(
    noinline block: suspend HttpClient.() -> HttpResponse,
): Either<DomainError, T> = try {
    val response = block()
    response.body<T>().right()
} catch (e: CancellationException) {
    throw e
} catch (e: HttpRequestTimeoutException) {
    DomainError.Timeout(e).left()
} catch (e: ResponseException) {
    DomainError.HttpError(e.response.status.value, e.response.bodyAsText()).left()
} catch (e: Throwable) {
    val networkError = when (e) {
        is UnknownHostException, is UnresolvedAddressException, is IOException -> DomainError.ConnectionError(e)
        is SerializationException, is JsonConvertException -> DomainError.SerializationError(e)
        else -> DomainError.UnknownError(e)
    }
    networkError.left()
}

internal val AuthenticatedRequest = AttributeKey<Unit>("AuthenticatedRequest")

internal fun HttpRequestBuilder.requiresAuth() {
    attributes.put(AuthenticatedRequest, Unit)
}
