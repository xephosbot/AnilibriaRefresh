package com.xbot.network.utils

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.DEFAULT_PORT
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.takeFrom
import io.ktor.util.appendAll
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlin.coroutines.cancellation.CancellationException

class MultiBaseUrlConfig : HttpMessageBuilder {
    internal val baseUrls = mutableListOf<Url>()
    override val headers: HeadersBuilder = HeadersBuilder()

    fun url(urlString: String) {
        baseUrls.add(Url(urlString))
    }

    fun urls(vararg urlStrings: String) {
        urlStrings.forEach { url(it) }
    }

    fun headers(block: HeadersBuilder.() -> Unit) {
        headers.apply(block)
    }
}

val MultiBaseUrlRequest = createClientPlugin("MultiBaseUrlRequest", ::MultiBaseUrlConfig) {
    val baseUrls = pluginConfig.baseUrls
    val configHeaders = pluginConfig.headers
    
    val baseUrlIndex = atomic(0)

    if (baseUrls.isEmpty()) return@createClientPlugin

    on(Send) { request ->
        if (request.url.host.isNotEmpty()) {
            return@on proceed(request)
        }

        val content = request.body as? io.ktor.http.content.OutgoingContent
        val isRetryableBody = content == null ||
                content is io.ktor.http.content.OutgoingContent.NoContent ||
                content is io.ktor.http.content.OutgoingContent.ByteArrayContent ||
                content is io.ktor.http.content.OutgoingContent.ProtocolUpgrade

        if (!isRetryableBody) {
            val currentBaseUrl = baseUrls[baseUrlIndex.value % baseUrls.size]
            applyBaseUrl(request, currentBaseUrl, configHeaders)
            return@on proceed(request)
        }

        val originalPathSegments = request.url.encodedPathSegments
        val originalParameters = ParametersBuilder().apply { appendAll(request.url.encodedParameters) }
        val originalFragment = request.url.encodedFragment

        var attempts = 0
        val maxAttempts = baseUrls.size

        while (currentCoroutineContext().isActive && attempts < maxAttempts) {
            attempts++

            val localIndex = baseUrlIndex.value
            val currentBaseUrl = baseUrls[localIndex % baseUrls.size]

            applyBaseUrl(request, currentBaseUrl, configHeaders)

            val tempUrl = URLBuilder().apply {
                encodedPathSegments = originalPathSegments
                encodedParameters.appendAll(originalParameters)
                encodedFragment = originalFragment
            }
            mergeUrls(currentBaseUrl, tempUrl)
            request.url.takeFrom(tempUrl)

            try {
                return@on proceed(request)
            } catch (cause: Throwable) {
                if (cause is CancellationException) throw cause

                if (isSocketTimeoutException(cause) && attempts < maxAttempts) {
                    baseUrlIndex.compareAndSet(localIndex, localIndex + 1)
                    continue
                } else {
                    throw cause
                }
            }
        }
        throw ConnectTimeoutException("All base URLs failed")
    }
}

private fun isSocketTimeoutException(cause: Throwable): Boolean {
    return when (cause) {
        is SocketTimeoutException -> true
        is ConnectTimeoutException -> true
        else -> {
            val rootCause = cause.cause
            rootCause != null && rootCause != cause && isSocketTimeoutException(rootCause)
        }
    }
}

private fun applyBaseUrl(
    request: HttpRequestBuilder,
    baseUrl: Url,
    configHeaders: HeadersBuilder
) {
    val defaultRequestHeaders = HeadersBuilder().apply { appendAll(configHeaders) }
    val userHeaders = request.headers.build()

    userHeaders.entries().forEach { (key, oldValues) ->
        val newValues = defaultRequestHeaders.getAll(key)
        if (newValues == null) {
            defaultRequestHeaders.appendAll(key, oldValues)
            return@forEach
        }
        if (newValues == oldValues || key == HttpHeaders.Cookie) return@forEach

        defaultRequestHeaders.remove(key)
        defaultRequestHeaders.appendAll(key, oldValues)
        defaultRequestHeaders.appendMissing(key, newValues)
    }

    request.headers.clear()
    request.headers.appendAll(defaultRequestHeaders.build())
}

private fun mergeUrls(baseUrl: Url, requestUrl: URLBuilder) {
    if (requestUrl.protocolOrNull == null) {
        requestUrl.protocolOrNull = baseUrl.protocolOrNull
    }

    if (requestUrl.host.isNotEmpty()) return

    val resultUrl = URLBuilder(baseUrl)
    with(requestUrl) {
        resultUrl.protocolOrNull = requestUrl.protocolOrNull
        if (port != DEFAULT_PORT) {
            resultUrl.port = port
        }

        resultUrl.encodedPathSegments = concatenatePath(resultUrl.encodedPathSegments, encodedPathSegments)

        if (encodedFragment.isNotEmpty()) {
            resultUrl.encodedFragment = encodedFragment
        }

        val defaultParameters = ParametersBuilder().apply {
            appendAll(resultUrl.encodedParameters)
        }

        resultUrl.encodedParameters = encodedParameters
        defaultParameters.entries().forEach { (key, values) ->
            if (!resultUrl.encodedParameters.contains(key)) {
                resultUrl.encodedParameters.appendAll(key, values)
            }
        }
        takeFrom(resultUrl)
    }
}

private fun concatenatePath(parent: List<String>, child: List<String>): List<String> {
    if (child.isEmpty()) return parent
    if (parent.isEmpty()) return child

    // Path starts from "/"
    if (child.first().isEmpty()) return child

    return buildList(parent.size + child.size - 1) {
        for (index in 0 until parent.size - 1) {
            add(parent[index])
        }

        addAll(child)
    }
}
