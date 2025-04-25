package com.xbot.shared.data.sources.remote

import com.xbot.shared.data.sources.remote.api.AnilibriaApi
import com.xbot.shared.data.sources.remote.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Client for interacting with the Anilibria API.
 *
 * This class provides a convenient way to make requests to the Anilibria API.
 * It handles the underlying HTTP communication, content negotiation, and error handling.
 *
 * @see <a href="https://anilibria.top/api/docs/v1">Anilibria API Documentation</a>
 */
class AnilibriaClient(private val tokenProvider: TokenProvider) {
    private val client = HttpClient {
        expectSuccess = true
        install(Logging) {
            logger = Logger.Companion.DEFAULT
            level = LogLevel.NONE
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            )
        }
        install(ContentEncoding) {
            gzip()
            brotli()
        }
        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getToken()?.let { accessToken ->
                        BearerTokens(accessToken, null)
                    }
                }
            }
        }
        install(DefaultRequest.Plugin) {
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