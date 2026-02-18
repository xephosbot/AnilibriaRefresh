package com.xbot.network.client

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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

internal suspend inline fun <reified T> HttpClient.request(
    block: HttpClient.() -> HttpResponse
): Either<NetworkError, T> = try {
    val response = block()
    response.body<T>().right()
} catch (e: CancellationException) {
    throw e
} catch (e: ResponseException) {
    NetworkError.HttpError(e.response.status.value, e.response.bodyAsText()).left()
} catch (e: Throwable) {
    val networkError = when (e) {
        is UnknownHostException, is UnresolvedAddressException, is IOException -> NetworkError.ConnectionError(e)
        is SerializationException, is JsonConvertException -> NetworkError.SerializationError(e)
        else -> NetworkError.UnknownError(e)
    }
    networkError.left()
}

internal val AuthenticatedRequest = AttributeKey<Unit>("AuthenticatedRequest")

internal fun HttpRequestBuilder.requiresAuth() {
    attributes.put(AuthenticatedRequest, Unit)
}
