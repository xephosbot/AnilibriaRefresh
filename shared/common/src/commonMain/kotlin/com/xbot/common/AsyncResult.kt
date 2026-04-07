package com.xbot.common

sealed interface AsyncResult<out E, out T> {
    data class Success<out T>(val data: T) : AsyncResult<Nothing, T>
    data class Error<out E>(val error: E) : AsyncResult<E, Nothing>
    object Loading : AsyncResult<Nothing, Nothing>
}

fun <T> AsyncResult<*, T>.getOrElse(default: () -> T): T = (this as? AsyncResult.Success<T>)?.data ?: default()

fun <E, T> AsyncResult<E, T>.consumeError(onError: (E) -> Unit): AsyncResult<E, T> = when (this) {
    is AsyncResult.Error -> { onError(error); AsyncResult.Loading }
    else -> this
}
