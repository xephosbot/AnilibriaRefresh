package com.xbot.common

import arrow.core.Either
import org.orbitmvi.orbit.syntax.Syntax

suspend fun <T, E, S : Any, SE : Any> Syntax<S, SE>.asyncLoad(
    request: suspend () -> Either<E, T>,
    reducer: S.(AsyncResult<E, T>) -> S
) {
    reduce { state.reducer(AsyncResult.Loading) }

    val result = request()
    reduce {
        val asyncResult = result.fold(
            ifLeft = { AsyncResult.Error(it) },
            ifRight = { AsyncResult.Success(it) }
        )
        state.reducer(asyncResult)
    }
}
