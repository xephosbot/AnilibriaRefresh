package com.xbot.common

import arrow.core.Either
import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Error
import io.nlopez.asyncresult.Loading
import io.nlopez.asyncresult.Success
import kotlinx.coroutines.flow.Flow

inline fun <E, T1, T2, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult()
    )
}

inline fun <E, T1, T2, T3, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
    )
}

inline fun <E, T1, T2, T3, T4, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>, AsyncResult<T4>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
        results[3].value.toAsyncResult(),
    )
}

inline fun <E, T1, T2, T3, T4, T5, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>, AsyncResult<T4>, AsyncResult<T5>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
        results[3].value.toAsyncResult(),
        results[4].value.toAsyncResult(),
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>, AsyncResult<T4>, AsyncResult<T5>, AsyncResult<T6>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
        results[3].value.toAsyncResult(),
        results[4].value.toAsyncResult(),
        results[5].value.toAsyncResult(),
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, T7, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline fetcher7: suspend () -> Either<E, T7>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>, AsyncResult<T4>, AsyncResult<T5>, AsyncResult<T6>, AsyncResult<T7>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() }, { fetcher7() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
        results[3].value.toAsyncResult(),
        results[4].value.toAsyncResult(),
        results[5].value.toAsyncResult(),
        results[6].value.toAsyncResult(),
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, T7, T8, R> combinePartial2(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline fetcher7: suspend () -> Either<E, T7>,
    crossinline fetcher8: suspend () -> Either<E, T8>,
    crossinline combine: (AsyncResult<T1>, AsyncResult<T2>, AsyncResult<T3>, AsyncResult<T4>, AsyncResult<T5>, AsyncResult<T6>, AsyncResult<T7>, AsyncResult<T8>) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() }, { fetcher7() }, { fetcher8() })
) { results ->
    combine(
        results[0].value.toAsyncResult(),
        results[1].value.toAsyncResult(),
        results[2].value.toAsyncResult(),
        results[3].value.toAsyncResult(),
        results[4].value.toAsyncResult(),
        results[5].value.toAsyncResult(),
        results[6].value.toAsyncResult(),
        results[7].value.toAsyncResult()
    )
}

fun <T> Any?.toAsyncResult(): AsyncResult<T> = when (this) {
    null -> Loading
    is Either<*, *> -> fold(
        ifLeft = { error ->
            Error(
                throwable = error as? Throwable,
                metadata = error.takeUnless { it is Throwable }
            )
        },
        ifRight = { Success(it as T) }
    )
    else -> Error(IllegalStateException("Unexpected: $this"))
}