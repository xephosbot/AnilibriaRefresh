package com.xbot.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * An Effect to provide a result even between different screens
 *
 * The trailing lambda provides the result from a flow of results.
 *
 * @param resultEventBus the ResultEventBus to retrieve the result from
 * @param resultKey the key that should be associated with this effect
 * @param onResult the callback to invoke when a result is received
 */
@Composable
inline fun <reified T> ResultEffect(
    resultEventBus: ResultEventBus = LocalResultEventBus.current,
    resultKey: String = T::class.toString(),
    crossinline onResult: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(resultKey, resultEventBus.channelMap[resultKey]) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            resultEventBus.getResultFlow<T>(resultKey)?.collect { result ->
                onResult.invoke(result as T)
            }
        }
    }
}

/**
 * Local for receiving results in a [ResultEventBus]
 */
object LocalResultEventBus {
    private val LocalResultEventBus: ProvidableCompositionLocal<ResultEventBus?> =
        compositionLocalOf { null }

    /**
     * The current [ResultEventBus]
     */
    val current: ResultEventBus
        @Composable
        get() = LocalResultEventBus.current ?: error("No ResultStore has been provided")

    /**
     * Provides a [ResultEventBus] to the composition
     */
    infix fun provides(bus: ResultEventBus): ProvidedValue<ResultEventBus?> {
        return LocalResultEventBus.provides(bus)
    }
}

/**
 * An EventBus for passing results between multiple sets of screens.
 *
 * It provides a solution for event based results.
 */
class ResultEventBus {
    /**
     * Map from the result key to a channel of results.
     */
    val channelMap: MutableMap<String, Channel<Any?>> = mutableMapOf()

    /**
     * Provides a flow for the given resultKey.
     */
    inline fun <reified T> getResultFlow(resultKey: String = T::class.toString()) =
        channelMap[resultKey]?.receiveAsFlow()

    /**
     * Sends a result into the channel associated with the given resultKey.
     */
    inline fun <reified T> sendResult(resultKey: String = T::class.toString(), result: T) {
        if (!channelMap.contains(resultKey)) {
            channelMap.put(resultKey, Channel(capacity = BUFFERED, onBufferOverflow = BufferOverflow.SUSPEND))
        }
        channelMap[resultKey]?.trySend(result)
    }

    /**
     * Removes all results associated with the given key from the store.
     */
    inline fun <reified T> removeResult(resultKey: String = T::class.toString()) {
        channelMap.remove(resultKey)
    }
}