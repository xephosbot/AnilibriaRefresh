package com.xbot.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton

/**
 * Android connectivity monitor backed by [ConnectivityManager.NetworkCallback].
 *
 * Registered once at app startup as a Koin singleton; the callback stays alive for the
 * process lifetime. [currentState] is maintained as a `MutableStateFlow`, keeping the
 * synchronous read (used by the HTTP pre-flight check) cheap and allocation-free.
 *
 * Requires `android.permission.ACCESS_NETWORK_STATE` in the manifest.
 */
@Singleton
internal class AndroidConnectivityMonitor(
    context: Context,
) : ConnectivityMonitor {

    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val state = MutableStateFlow(readInitialState())

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Availability alone is not enough — wait for capability confirmation below.
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            state.value = if (capabilities.isValidated()) {
                ConnectivityState.Available
            } else {
                ConnectivityState.Unavailable
            }
        }

        override fun onLost(network: Network) {
            state.value = ConnectivityState.Unavailable
        }

        override fun onUnavailable() {
            state.value = ConnectivityState.Unavailable
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        runCatching { connectivityManager.registerNetworkCallback(request, callback) }
    }

    override val currentState: ConnectivityState
        get() = state.value

    override fun observe(): Flow<ConnectivityState> = state.asStateFlow()

    private fun readInitialState(): ConnectivityState {
        val active = connectivityManager.activeNetwork ?: return ConnectivityState.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(active)
            ?: return ConnectivityState.Unavailable
        return if (capabilities.isValidated()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    }

    private fun NetworkCapabilities.isValidated(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
