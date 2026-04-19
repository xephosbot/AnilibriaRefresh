package com.xbot.network.connectivity

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_global_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT

/**
 * iOS connectivity monitor backed by `NWPathMonitor` (Network framework).
 *
 * Starts in `init` and runs for the lifetime of the singleton (app-lifetime). The
 * monitor updates a [MutableStateFlow] so [currentState] stays cheap for use as a
 * pre-flight check in the HTTP layer.
 */
@OptIn(ExperimentalForeignApi::class)
@Singleton
internal class IosConnectivityMonitor : ConnectivityMonitor {

    private val state = MutableStateFlow(ConnectivityState.Available)

    private val monitor: nw_path_monitor_t = nw_path_monitor_create()

    init {
        nw_path_monitor_set_update_handler(monitor) { path ->
            state.value = if (nw_path_get_status(path) == nw_path_status_satisfied) {
                ConnectivityState.Available
            } else {
                ConnectivityState.Unavailable
            }
        }
        nw_path_monitor_set_queue(
            monitor,
            dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u),
        )
        nw_path_monitor_start(monitor)
    }

    override val currentState: ConnectivityState
        get() = state.value

    override fun observe(): Flow<ConnectivityState> = state.asStateFlow()
}
