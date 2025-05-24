package com.xbot.api.di

import com.xbot.api.AnilibriaApi
import com.xbot.api.client.AnilibriaClient
import com.xbot.api.client.AccessTokenManager
import com.xbot.api.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single { AccessTokenManager(get()) }

    single {
        HttpClient {
            expectSuccess = true
            followRedirects = true

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO
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
                        val tokenManager: AccessTokenManager = get()
                        val accessToken = tokenManager.getAccessToken()
                        if (accessToken != null) BearerTokens(accessToken, null) else null
                    }
                    sendWithoutRequest { request ->
                        request.url.encodedPath == "/api/v1/accounts/users/me"
                    }
                }
            }

            install(DefaultRequest) {
                url(AnilibriaApi.BASE_URL_API)
                contentType(ContentType.Application.Json)
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
        }
    }

    single { AnilibriaClient(get()) }
}