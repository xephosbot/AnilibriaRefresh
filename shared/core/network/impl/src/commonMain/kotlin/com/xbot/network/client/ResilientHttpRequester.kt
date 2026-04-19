package com.xbot.network.client

import arrow.core.Either
import arrow.core.left
import arrow.resilience.Schedule
import arrow.resilience.retryEither
import com.xbot.domain.models.DomainError
import com.xbot.network.connectivity.ConnectivityMonitor
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import org.koin.core.annotation.Singleton

/**
 * Central entry point for every HTTP call in the app.
 *
 * Combines three resilience layers:
 *
 * 1. **Connectivity pre-flight** — if [connectivity] reports offline, we short-circuit
 *    with [DomainError.NoConnection] before touching the wire. Different UX copy and
 *    recovery path than [DomainError.ConnectionError] (which comes from a failed
 *    socket).
 * 2. **Per-attempt timeout** — bounded by the [io.ktor.client.plugins.HttpTimeout]
 *    plugin installed on the underlying [client]; hung sockets surface as
 *    [DomainError.Timeout].
 * 3. **Typed retries** — [schedule] drives exponential-with-jitter backoff and uses
 *    [DomainError.isRetryable] as the gate. Auth errors (401/403) are NOT retryable
 *    here, so we don't fight the Ktor Auth plugin's refresh flow.
 */
@Singleton
internal class ResilientHttpRequester(
    private val client: HttpClient,
    private val connectivity: ConnectivityMonitor,
    private val schedule: Schedule<DomainError, *>,
) {
    /**
     * Execute [block] on the underlying [HttpClient] and decode the response as [T].
     *
     * Every failure mode — network hiccup, 5xx, body parse error, timeout, offline —
     * is expressed as [Either.Left] of the matching [DomainError] variant. No throw.
     */
    suspend inline fun <reified T> request(
        noinline block: suspend HttpClient.() -> HttpResponse,
    ): Either<DomainError, T> {
        if (!connectivity.isOnline()) return DomainError.NoConnection.left()
        return schedule.retryEither { client.singleAttempt<T>(block) }
    }
}
