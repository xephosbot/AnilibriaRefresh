package com.xbot.network.utils

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

actual fun createNetworkObserver(): NetworkObserver {
    return IosNetworkObserver()
}

internal class IosNetworkObserver : NetworkObserver {

    override val isConnected: Flow<Boolean> = callbackFlow {
        val monitor = nw_path_monitor_create()

        val queue = dispatch_queue_create("com.xbot.network.monitor", null)
        nw_path_monitor_set_queue(monitor, queue)

        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            val isSatisfied = status == nw_path_status_satisfied

            trySend(isSatisfied)
        }

        nw_path_monitor_start(monitor)

        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }
        .distinctUntilChanged()
}
