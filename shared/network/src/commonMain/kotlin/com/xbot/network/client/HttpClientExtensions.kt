package com.xbot.network.client

import arrow.core.Either
import arrow.core.raise.either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.network.*
import kotlinx.serialization.SerializationException

internal suspend inline fun <reified T> HttpClient.request(
    block: HttpClient.() -> HttpResponse
): Either<NetworkError, T> = either {
    try {
        val response = this@request.block()
        when (response.status) {
            HttpStatusCode.OK -> response.body<T>()
            else -> {
                raise(NetworkError.HttpError(response.status.value, response.status.description))
            }
        }
    } catch (e: SerializationException) {
        raise(NetworkError.SerializationError(e))
    } catch (e: UnresolvedAddressException) {
        raise(NetworkError.NetworkException(e))
    }
}

sealed class NetworkError {
    data class HttpError(val code: Int, val message: String?) : NetworkError()
    data class SerializationError(val cause: Throwable) : NetworkError()
    data class NetworkException(val cause: Throwable) : NetworkError()
}