package com.xbot.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import org.orbitmvi.orbit.syntax.Syntax

data class LoadableField<S : Any>(
    val selector: (S) -> AsyncResult<*, *>,
    val load: () -> Job
)

fun <S : Any> List<LoadableField<S>>.hasErrors(state: S): Boolean =
    any { it.selector(state) is AsyncResult.Error }

fun <S : Any> List<LoadableField<S>>.isLoading(state: S): Boolean =
    any { it.selector(state) is AsyncResult.Loading }

fun <S : Any> List<LoadableField<S>>.allSucceeded(state: S): Boolean =
    all { it.selector(state) is AsyncResult.Success }

fun <S : Any> List<LoadableField<S>>.firstError(state: S): Throwable? =
    firstNotNullOfOrNull { field ->
        when (val error = (field.selector(state) as? AsyncResult.Error<*>)?.error) {
            is Throwable -> error
            null -> null
            else -> RuntimeException("Unexpected error type: $error")
        }
    }

suspend fun <S : Any, SE : Any> Syntax<S, SE>.retryErrors(
    fields: List<LoadableField<S>>,
    onBatchFailure: (suspend Syntax<S, SE>.() -> Unit)? = null
) {
    fields
        .filter { it.selector(state) is AsyncResult.Error }
        .map { it.load() }
        .joinAll()
    if (fields.hasErrors(state)) onBatchFailure?.invoke(this)
}

suspend fun <S : Any, SE : Any> Syntax<S, SE>.refreshAll(
    fields: List<LoadableField<S>>,
    onBatchFailure: (suspend Syntax<S, SE>.() -> Unit)? = null
) {
    fields.map { it.load() }.joinAll()
    if (fields.hasErrors(state)) onBatchFailure?.invoke(this)
}
