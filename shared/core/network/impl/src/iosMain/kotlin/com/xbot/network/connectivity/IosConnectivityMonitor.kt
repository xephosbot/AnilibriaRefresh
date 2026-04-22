package com.xbot.network.connectivity

import com.xbot.common.DispatcherProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Singleton
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_global_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT

/**
 * iOS connectivity monitor backed by `NWPathMonitor` (Network framework).
 */
@OptIn(ExperimentalForeignApi::class)
@Singleton
internal class IosConnectivityMonitor(
    dispatcherProvider: DispatcherProvider,
) : ConnectivityMonitor {

    private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)

    private val connectivityFlow: StateFlow<ConnectivityState> = callbackFlow {
        val monitor = nw_path_monitor_create()

        nw_path_monitor_set_update_handler(monitor) { path ->
            val newState = if (nw_path_get_status(path) == nw_path_status_satisfied) {
                ConnectivityState.Available
            } else {
                ConnectivityState.Unavailable
            }
            trySend(newState)
        }

        nw_path_monitor_set_queue(
            monitor,
            dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u),
        )
        nw_path_monitor_start(monitor)

        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = ConnectivityState.Available
    )

    override val currentState: ConnectivityState
        get() = connectivityFlow.value

    override fun observe(): Flow<ConnectivityState> = connectivityFlow
}
