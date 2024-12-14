package com.xbot.api.di

import com.xbot.api.AnilibriaApi
import com.xbot.api.client.AnilibriaClient
import com.xbot.api.utils.brotli
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(OkHttp) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
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
            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, request ->
                    val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response
                }
            }
            install(DefaultRequest) {
                url(get<String>(named("baseUrlApi")))
                contentType(ContentType.Application.Json)
            }
        }
    }

    single {
        AnilibriaClient(client = get())
    }

    single(named("baseUrl")) { AnilibriaApi.BASE_URL }
    single(named("baseUrlApi")) { AnilibriaApi.BASE_URL_API }
}
