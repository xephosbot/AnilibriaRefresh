package com.xbot.network.client

import com.xbot.network.Constants
import com.xbot.network.utils.brotli
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HTTP client instances.
 */
fun createHttpClient(
    baseUrl: String = Constants.BASE_URL_API,
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient = HttpClient {
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
    install(DefaultRequest) {
        url(baseUrl)
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
    this.block()
}
