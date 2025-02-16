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
 * Client for interacting with the Anilibria API.
 *
 * This class provides a convenient way to make requests to the Anilibria API.
 * It handles the underlying HTTP communication, content negotiation, and error handling.
 *
 * @see <a href="https://anilibria.top/api/docs/v1">Anilibria API Documentation</a>
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

    /**
     * Executes a request using the provided [action] and deserializes the response body to type [T].
     *
     * This function is a helper for making requests using an [HttpClient] instance.
     * It internally uses `HttpResponse.body<T>()` to deserialize the response.
     *
     * @param T The type to which the response body should be deserialized.
     * @param action A lambda function that defines the request to be executed.
     *               This lambda takes an [HttpClient] as a receiver and returns an [HttpResponse].
     * @return The deserialized response body of type [T].
     * @throws Exception if the request fails or the response body cannot be deserialized.
     */
    internal suspend inline fun <reified T> request(
        action: HttpClient.() -> HttpResponse
    ): T {
        return client.action().body<T>()
    }
}