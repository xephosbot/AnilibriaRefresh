package com.xbot.network.client

import arrow.resilience.Schedule
import com.xbot.domain.models.DomainError
import kotlin.time.Duration.Companion.milliseconds
import org.koin.core.annotation.Singleton

/**
 * Default retry policy for transient network failures.
 *
 * **Shape:** exponential backoff starting at 200ms, factor 2, capped at 3 retries
 * (=4 total attempts), jittered to avoid retry storms, gated by [DomainError.isRetryable]
 * so only transient conditions (5xx, 408, 429, `Timeout`, `ConnectionError`) trigger
 * another attempt.
 *
 * Worst-case total delay pre-jitter: 200 + 400 + 800 ≈ 1.4s across 3 retries.
 * With the per-attempt 10s [io.ktor.client.plugins.HttpTimeout], an absolute worst-case
 * transaction is roughly 43s; in practice the jitter + transient recovery makes the
 * p50 retry path end within 300–500ms.
 *
 * Exposed as a Koin singleton so tests can replace it with a deterministic (no-jitter,
 * fewer retries) variant.
 */
@Singleton
internal fun networkRetrySchedule(): Schedule<DomainError, *> =
    Schedule.exponential<DomainError>(200.milliseconds, factor = 2.0)
        .and(Schedule.recurs(RETRIES))
        .jittered()
        .doWhile { error, _ -> error.isRetryable }

private const val RETRIES: Long = 3
