package com.xbot.network.client

import com.xbot.network.utils.isTimeoutException
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Suppress("FunctionName")
internal fun HttpClientConfig<*>.NetworkRetry(
    retryDelay: suspend (Long) -> Unit = { millis -> delay(millis.milliseconds) },
) {
    install(HttpRequestRetry) {
        maxRetries = MAX_RETRIES

        retryIf { _, response ->
            val status = response.status.value
            status in 500..599 || status == 429 || status == 408
        }

        retryOnExceptionIf { _, cause ->
            when {
                cause.isTimeoutException() -> true
                cause is CancellationException -> false
                else -> true
            }
        }

        exponentialDelay(
            base = RETRY_BACKOFF_FACTOR,
            baseDelayMs = RETRY_BASE_DELAY_MS,
            randomizationMs = RETRY_JITTER_MS,
        )

        delay(retryDelay)
    }
}

private const val MAX_RETRIES = 3

private const val RETRY_BASE_DELAY_MS = 200L
private const val RETRY_BACKOFF_FACTOR = 2.0
private const val RETRY_JITTER_MS = 200L
