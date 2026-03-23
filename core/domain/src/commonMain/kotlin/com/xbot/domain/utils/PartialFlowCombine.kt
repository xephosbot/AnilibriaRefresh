@file:Suppress("UNCHECKED_CAST", "unused")

package com.xbot.domain.utils

import arrow.core.Either
import kotlinx.atomicfu.AtomicArray
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

inline fun <E, T1, T2, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline combine: (T1?, T2?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value)
    )
}

inline fun <E, T1, T2, T3, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline combine: (T1?, T2?, T3?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value)
    )
}

inline fun <E, T1, T2, T3, T4, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline combine: (T1?, T2?, T3?, T4?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value),
        unwrapEither(results[3].value)
    )
}

inline fun <E, T1, T2, T3, T4, T5, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline combine: (T1?, T2?, T3?, T4?, T5?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value),
        unwrapEither(results[3].value),
        unwrapEither(results[4].value)
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline combine: (T1?, T2?, T3?, T4?, T5?, T6?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value),
        unwrapEither(results[3].value),
        unwrapEither(results[4].value),
        unwrapEither(results[5].value)
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, T7, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline fetcher7: suspend () -> Either<E, T7>,
    crossinline combine: (T1?, T2?, T3?, T4?, T5?, T6?, T7?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() }, { fetcher7() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value),
        unwrapEither(results[3].value),
        unwrapEither(results[4].value),
        unwrapEither(results[5].value),
        unwrapEither(results[6].value)
    )
}

inline fun <E, T1, T2, T3, T4, T5, T6, T7, T8, R> combinePartial(
    crossinline fetcher1: suspend () -> Either<E, T1>,
    crossinline fetcher2: suspend () -> Either<E, T2>,
    crossinline fetcher3: suspend () -> Either<E, T3>,
    crossinline fetcher4: suspend () -> Either<E, T4>,
    crossinline fetcher5: suspend () -> Either<E, T5>,
    crossinline fetcher6: suspend () -> Either<E, T6>,
    crossinline fetcher7: suspend () -> Either<E, T7>,
    crossinline fetcher8: suspend () -> Either<E, T8>,
    crossinline combine: (T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = arrayListOf({ fetcher1() }, { fetcher2() }, { fetcher3() }, { fetcher4() }, { fetcher5() }, { fetcher6() }, { fetcher7() }, { fetcher8() })
) { results ->
    combine(
        unwrapEither(results[0].value),
        unwrapEither(results[1].value),
        unwrapEither(results[2].value),
        unwrapEither(results[3].value),
        unwrapEither(results[4].value),
        unwrapEither(results[5].value),
        unwrapEither(results[6].value),
        unwrapEither(results[7].value)
    )
}

@PublishedApi
internal inline fun <R> combinePartialInternal(
    fetchers: List<suspend () -> Any?>,
    concurrency: Int = fetchers.size,
    crossinline combine: (AtomicArray<Any?>) -> R
): Flow<R> = channelFlow {
    val results = atomicArrayOfNulls<Any>(fetchers.size)
    val state = MutableStateFlow(combine(results))

    launch { state.collect { send(it) } }

    val semaphore = Semaphore(concurrency)

    coroutineScope {
        for (i in fetchers.indices) {
            launch {
                semaphore.withPermit {
                    val result = fetchers[i]()
                    results[i].value = result
                    state.update { combine(results) }
                }
            }
        }
    }
}

@PublishedApi
internal fun <T> unwrapEither(result: Any?): T? {
    return when (result) {
        is Either<*, *> -> result.fold(
            ifLeft = { error ->
                throw (error as? Throwable) ?: IllegalStateException(error.toString())
            },
            ifRight = { value -> value as? T }
        )
        else -> null
    }
}
