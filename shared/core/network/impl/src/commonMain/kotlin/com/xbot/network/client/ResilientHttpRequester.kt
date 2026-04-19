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
 * 1. **Per-attempt connectivity gate** — every retry tick checks
 *    [ConnectivityMonitor.isOnline] first; if offline we short-circuit with
 *    [DomainError.NoConnection] before touching the wire. Doing the check per-attempt
 *    (not just once before the loop) means a connection dropped mid-backoff surfaces
 *    as NoConnection on the very next tick, rather than burning the remaining retry
 *    budget on doomed [DomainError.ConnectionError]s. NoConnection is non-retryable,
 *    so the Schedule exits cleanly on first offline tick. Distinct from
 *    [DomainError.ConnectionError] (which comes from a socket that actually attempted
 *    a connection) — different UX copy, different recovery path.
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
    ): Either<DomainError, T> = schedule.retryEither {
        if (!connectivity.isOnline()) {
            DomainError.NoConnection.left()
        } else {
            client.singleAttempt<T>(block)
        }
    }
}
