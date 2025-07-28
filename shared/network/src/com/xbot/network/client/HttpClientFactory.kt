package com.xbot.network.client

import com.xbot.network.AnilibriaApi
import com.xbot.network.client.auth.AuthTokenManager
import com.xbot.network.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HTTP client instances.
 */
class HttpClientFactory(
    private val authTokenManager: AuthTokenManager
) {

    fun create() = HttpClient {
        expectSuccess = true
        followRedirects = true

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
            filter { request ->
                !request.url.encodedPath.contains("auth")
            }
        }

        install(ContentEncoding) {
            gzip()
            brotli()
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = authTokenManager.getToken()
                    token?.let {
                        BearerTokens(accessToken = it, refreshToken = null)
                    }
                }

                refreshTokens {
                    authTokenManager.clearToken()
                    null
                }
            }
        }

        defaultRequest {
            url(AnilibriaApi.BASE_URL_API)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        install(HttpRequestRetry) {
            maxRetries = 2
            retryIf { _, response ->
                response.status.value == 525 ||
                        response.status.value == HttpStatusCode.BadGateway.value
            }
            modifyRequest { request ->
                if (request.url.host == "anilibria.top") {
                    request.url.host = "anilibria.wtf"
                }
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                when (response.status.value) {
                    401 -> {
                        authTokenManager.clearToken()
                        throw IllegalStateException("Authentication failed")
                    }
                    403 -> {
                        throw IllegalStateException("Access forbidden")
                    }
                }
            }
        }
    }
}
