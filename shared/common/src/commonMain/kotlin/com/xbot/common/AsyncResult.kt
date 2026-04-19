package com.xbot.common

import arrow.core.Either

/**
 * Tri-state UI container: `Loading` while an async operation is in flight, `Success`
 * when it completed with a value, `Error` with a typed failure.
 *
 * This is intentionally distinct from Arrow's [Either]. Arrow models the outcome of
 * a computation (success/failure) — the "not yet" case is a UI concern that Arrow
 * does not express, and the candidates that come close (`Option<Either<E, T>>`,
 * `Ior`, `Either<AsyncFailure, T>`) all read worse at the use sites that drive
 * Compose placeholders. Keep the split clean: Arrow at the repository boundary,
 * [AsyncResult] in UI state — bridged by [toAsyncResult].
 */
sealed interface AsyncResult<out E, out T> {
    data class Success<out T>(val data: T) : AsyncResult<Nothing, T>
    data class Error<out E>(val error: E) : AsyncResult<E, Nothing>
    data object Loading : AsyncResult<Nothing, Nothing>
}

fun <T> AsyncResult<*, T>.getOrElse(default: () -> T): T = (this as? AsyncResult.Success<T>)?.data ?: default()

fun <T> AsyncResult<*, T>.getOrNull(): T? = (this as? AsyncResult.Success<T>)?.data

inline fun <E, T, R> AsyncResult<E, T>.map(transform: (T) -> R): AsyncResult<E, R> = when (this) {
    is AsyncResult.Success -> AsyncResult.Success(transform(data))
    is AsyncResult.Error -> this
    is AsyncResult.Loading -> this
}

/**
 * Bridge an Arrow computation outcome into a UI-ready [AsyncResult].
 *
 * The `Loading` state is never produced here — it's strictly a pre-completion marker
 * that the caller is expected to have set *before* awaiting the [Either]. Use this
 * at the end of the await step (typically in an Orbit `reduce {}`).
 */
fun <E, T> Either<E, T>.toAsyncResult(): AsyncResult<E, T> = fold(
    ifLeft = { AsyncResult.Error(it) },
    ifRight = { AsyncResult.Success(it) },
)

/**
 * Dispatch on the three states in a single expression — useful for Compose render
 * code that needs a value per branch.
 */
inline fun <E, T, R> AsyncResult<E, T>.fold(
    onLoading: () -> R,
    onError: (E) -> R,
    onSuccess: (T) -> R,
): R = when (this) {
    AsyncResult.Loading -> onLoading()
    is AsyncResult.Error -> onError(error)
    is AsyncResult.Success -> onSuccess(data)
}

/**
 * Run [action] only on [AsyncResult.Success], pass the receiver through unchanged —
 * chains cleanly with [onError] / [onLoading].
 */
inline fun <E, T> AsyncResult<E, T>.onSuccess(action: (T) -> Unit): AsyncResult<E, T> = also {
    if (this is AsyncResult.Success) action(data)
}

/**
 * Run [action] only on [AsyncResult.Error].
 */
inline fun <E, T> AsyncResult<E, T>.onError(action: (E) -> Unit): AsyncResult<E, T> = also {
    if (this is AsyncResult.Error) action(error)
}

/**
 * Run [action] only on [AsyncResult.Loading].
 */
inline fun <E, T> AsyncResult<E, T>.onLoading(action: () -> Unit): AsyncResult<E, T> = also {
    if (this is AsyncResult.Loading) action()
}
