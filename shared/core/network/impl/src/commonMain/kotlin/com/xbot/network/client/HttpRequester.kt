package com.xbot.network.client

import arrow.core.Either
import arrow.core.raise.either
import com.xbot.common.error.AppError
import com.xbot.logger.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import org.koin.core.annotation.Factory

@Factory
internal class HttpRequester(
    private val client: Lazy<HttpClient>,
    private val logger: Lazy<AppLogger>,
) {
    suspend inline fun <reified T> request(
        noinline block: suspend HttpClient.() -> HttpResponse,
    ): Either<AppError, T> = either {
        client.value.request<T>(block)
    }.onLeft { reportIfUnknown(it) }

    internal fun reportIfUnknown(error: AppError) {
        if (error is AppError.UnknownError) {
            logger.value.reportError(error.cause, "Unhandled network error")
        }
    }
}
