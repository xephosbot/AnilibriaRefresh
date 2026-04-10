package com.xbot.common

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
