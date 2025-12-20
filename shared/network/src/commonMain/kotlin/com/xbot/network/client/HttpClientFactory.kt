package com.xbot.network.client

import com.xbot.network.Constants
import com.xbot.network.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.logger.Logger
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * Factory for creating configured HTTP client instances.
 */
internal fun createHttpClient(
    tokenStorage: TokenStorage,
    logger: Logger
): HttpClient = HttpClient {
    expectSuccess = true

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }

    install(HttpCookies) {
        storage = SessionCookieStorage(tokenStorage)
    }

    install(Logging) {
        this.logger = logger.toKtorLogger()
        this.level = LogLevel.INFO
    }

    install(ContentEncoding) {
        gzip()
        brotli()
    }

    install(DefaultRequest) {
        url(Constants.BASE_URL_API)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }
}

private fun Logger.toKtorLogger(): KtorLogger = object : KtorLogger {
    override fun log(message: String) = this@toKtorLogger.info(message)
}
