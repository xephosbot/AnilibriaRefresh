@file:Suppress("UNCHECKED_CAST", "unused")

package com.xbot.domain.utils

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun <T1, T2, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    combine: (T1?, T2?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2
    )
}

fun <T1, T2, T3, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    combine: (T1?, T2?, T3?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3
    )
}

fun <T1, T2, T3, T4, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    fetcher4: suspend () -> T4,
    combine: (T1?, T2?, T3?, T4?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3, fetcher4)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3,
        results[3] as? T4
    )
}

fun <T1, T2, T3, T4, T5, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    fetcher4: suspend () -> T4,
    fetcher5: suspend () -> T5,
    combine: (T1?, T2?, T3?, T4?, T5?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3, fetcher4, fetcher5)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3,
        results[3] as? T4,
        results[4] as? T5
    )
}

fun <T1, T2, T3, T4, T5, T6, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    fetcher4: suspend () -> T4,
    fetcher5: suspend () -> T5,
    fetcher6: suspend () -> T6,
    combine: (T1?, T2?, T3?, T4?, T5?, T6?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3, fetcher4, fetcher5, fetcher6)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3,
        results[3] as? T4,
        results[4] as? T5,
        results[5] as? T6
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    fetcher4: suspend () -> T4,
    fetcher5: suspend () -> T5,
    fetcher6: suspend () -> T6,
    fetcher7: suspend () -> T7,
    combine: (T1?, T2?, T3?, T4?, T5?, T6?, T7?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3, fetcher4, fetcher5, fetcher6, fetcher7)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3,
        results[3] as? T4,
        results[4] as? T5,
        results[5] as? T6,
        results[6] as? T7
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combinePartial(
    fetcher1: suspend () -> T1,
    fetcher2: suspend () -> T2,
    fetcher3: suspend () -> T3,
    fetcher4: suspend () -> T4,
    fetcher5: suspend () -> T5,
    fetcher6: suspend () -> T6,
    fetcher7: suspend () -> T7,
    fetcher8: suspend () -> T8,
    combine: (T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?) -> R
): Flow<R> = combinePartialInternal(
    fetchers = listOf(fetcher1, fetcher2, fetcher3, fetcher4, fetcher5, fetcher6, fetcher7, fetcher8)
) { results ->
    combine(
        results[0] as? T1,
        results[1] as? T2,
        results[2] as? T3,
        results[3] as? T4,
        results[4] as? T5,
        results[5] as? T6,
        results[6] as? T7,
        results[7] as? T8
    )
}

private fun <R> combinePartialInternal(
    fetchers: List<suspend () -> Any?>,
    combine: (List<Any?>) -> R
): Flow<R> = channelFlow {
    val results = AtomicList(List<Any?>(fetchers.size) { null })
    val state = MutableStateFlow(combine(results.get()))

    launch { state.collect { send(it) } }

    coroutineScope {
        fetchers.forEachIndexed { index, fetcher ->
            launch {
                val result = fetcher()
                results[index] = result
                state.update { combine(results.get()) }
            }
        }
    }
}

private class AtomicList<T>(initialList: List<T>) {
    private val listRef = atomic(initialList)

    operator fun set(index: Int, element: T) {
        listRef.update { list ->
            val newList = list.toMutableList()
            newList[index] = element
            newList
        }
    }

    fun get(): List<T> = listRef.value
}
