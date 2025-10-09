package com.xbot.network.client

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

expect class UnknownHostException : Exception

internal suspend inline fun <reified T> HttpClient.request(
    block: HttpClient.() -> HttpResponse
): Either<NetworkError, T> = Either.catch {
    this@request.block().body<T>()
}.mapLeft { e ->
    if (e is CancellationException) {
        throw e
    } else {
        when (e) {
            is ClientRequestException -> NetworkError.HttpError(e.response.status.value, e.message)
            is UnknownHostException, is UnresolvedAddressException -> NetworkError.ConnectionError(e)
            is SerializationException -> NetworkError.SerializationError(e)
            else -> NetworkError.UnknownError(e)
        }
    }
}