package com.xbot.network.di

import com.xbot.network.Constants
import com.xbot.network.client.AuthenticatedRequest
import com.xbot.network.client.SessionStorage
import com.xbot.network.client.NetworkRetry
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

@Module
@Configuration
@ComponentScan("com.xbot.network")
class NetworkModule {

    @Singleton
    internal fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    internal fun createHttpClient(
        sessionStorage: Lazy<SessionStorage>,
        json: Json,
    ): HttpClient = HttpClient {
        expectSuccess = true

        defaultRequest {
            url(Constants.BASE_URL_API)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(json)
        }

        NetworkRetry()

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 5_000
            socketTimeoutMillis = 10_000
        }

        Auth {
            bearer {
                cacheTokens = false
                loadTokens {
                    sessionStorage.value.getToken()
                }
                refreshTokens {
                    sessionStorage.value.clearToken()
                    null
                }
                sendWithoutRequest { request ->
                    request.attributes.contains(AuthenticatedRequest)
                }
            }
        }

        Logging {
            this.logger = object : KtorLogger {
                private val log = KermitLogger.withTag("Ktor")
                override fun log(message: String) {
                    log.i { message }
                }
            }
            this.level = LogLevel.INFO
        }

        ContentEncoding {
            gzip()
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response

                if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                    sessionStorage.value.clearToken()
                }
            }
        }
    }
}
