package com.xbot.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.xbot.common.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Singleton
import java.util.concurrent.ConcurrentHashMap

/**
 * Android connectivity monitor backed by [ConnectivityManager.NetworkCallback].
 */
@Singleton
internal class AndroidConnectivityMonitor(
    context: Context,
    dispatcherProvider: DispatcherProvider,
) : ConnectivityMonitor {

    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)

    private val connectivityFlow: StateFlow<ConnectivityState> = callbackFlow {
        val validatedNetworks: MutableSet<Network> = ConcurrentHashMap.newKeySet()

        fun publish() {
            val newState = if (validatedNetworks.isNotEmpty()) {
                ConnectivityState.Available
            } else {
                ConnectivityState.Unavailable
            }
            trySend(newState)
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                validatedNetworks.add(network)
                publish()
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                if (capabilities.isValidated()) {
                    validatedNetworks.add(network)
                } else {
                    validatedNetworks.remove(network)
                }
                publish()
            }

            override fun onLost(network: Network) {
                validatedNetworks.remove(network)
                publish()
            }

            override fun onUnavailable() {
                validatedNetworks.clear()
                publish()
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
        } catch (e: Exception) {
            trySend(ConnectivityState.Available)
        }

        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }.onStart {
        emit(readInitialState())
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = ConnectivityState.Available
    )

    override val currentState: ConnectivityState
        get() = connectivityFlow.value

    override fun observe(): Flow<ConnectivityState> = connectivityFlow

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    private fun readInitialState(): ConnectivityState = try {
        val active = connectivityManager.activeNetwork ?: return ConnectivityState.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(active)
            ?: return ConnectivityState.Unavailable
        if (capabilities.isValidated()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    } catch (e: Exception) {
        ConnectivityState.Available
    }

    private fun NetworkCapabilities.isValidated(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
