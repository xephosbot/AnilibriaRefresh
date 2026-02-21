package com.xbot.network.client

import co.touchlab.kermit.Logger as KermitLogger
import com.xbot.network.Constants
import com.xbot.network.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
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
import org.koin.core.annotation.Singleton
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * Factory for creating configured HTTP client instances.
 */
@Singleton
internal fun createHttpClient(
    sessionStorage: SessionStorage
): HttpClient = HttpClient {
    expectSuccess = true

    defaultRequest {
        url(Constants.BASE_URL_API)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }

    Auth {
        bearer {
            cacheTokens = false
            loadTokens {
                sessionStorage.getToken()
            }
            refreshTokens {
                sessionStorage.clearToken()
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
        brotli()
    }

    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, _ ->
            val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
            val exceptionResponse = clientException.response

            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                sessionStorage.clearToken()
            }
        }
    }
}
