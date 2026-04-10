package com.xbot.common

import arrow.core.Either
import org.orbitmvi.orbit.syntax.Syntax

suspend fun <T, E, S : Any, SE : Any> Syntax<S, SE>.asyncLoad(
    request: suspend () -> Either<E, T>,
    onError: (suspend (E) -> Unit)? = null,
    reducer: S.(AsyncResult<E, T>) -> S
) {
    reduce { state.reducer(AsyncResult.Loading) }

    request().fold(
        ifLeft = { error ->
            if (onError != null) {
                onError(error)
            } else {
                reduce { state.reducer(AsyncResult.Error(error)) }
            }
        },
        ifRight = { data ->
            reduce { state.reducer(AsyncResult.Success(data)) }
        }
    )
}
