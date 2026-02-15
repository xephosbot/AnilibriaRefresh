package com.xbot.network.client

import com.xbot.network.Constants
import com.xbot.network.utils.NetworkReachability
import com.xbot.network.utils.brotli
import com.xbot.network.utils.createNetworkObserver
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
import org.koin.core.logger.Logger
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * Factory for creating configured HTTP client instances.
 */
internal fun createHttpClient(
    sessionStorage: SessionStorage,
    logger: Logger
): HttpClient = HttpClient {
    expectSuccess = true

    defaultRequest {
        url(Constants.FALLBACK_URL_API)
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

    install(NetworkReachability) {
        networkObserver = createNetworkObserver()
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
        this.logger = logger.toKtorLogger()
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

private fun Logger.toKtorLogger(): KtorLogger = object : KtorLogger {
    override fun log(message: String) = this@toKtorLogger.info(message)
}
