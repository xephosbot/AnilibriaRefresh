package com.xbot.api.client

import com.xbot.api.AnilibriaApi
import com.xbot.api.di.defaultJson
import com.xbot.api.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

/**
 * Клиент для взаимодействия с API Anilibria.
 * @see <a href="https://anilibria.top/api/docs/v1">Документация API Anilibria</a>
 */
class AnilibriaClient {
    private val client = HttpClient {
        expectSuccess = true
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(defaultJson)
        }
        install(ContentEncoding) {
            gzip()
            brotli()
        }
        install(HttpCallValidator) {
            handleResponseExceptionWithRequest { exception, request ->
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
            }
        }
        install(DefaultRequest) {
            url(AnilibriaApi.BASE_URL_API)
            contentType(ContentType.Application.Json)
        }
    }

    internal suspend inline fun <reified T> request(
        action: HttpClient.() -> HttpResponse
    ): T {
        return client.action().body<T>()
    }
}