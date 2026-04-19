package com.xbot.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton
import java.util.concurrent.ConcurrentHashMap

/**
 * Android connectivity monitor backed by [ConnectivityManager.NetworkCallback].
 *
 * A phone typically holds more than one active network at once (Wi-Fi + cellular), and
 * "online" means *at least one* is validated. We track the set of currently validated
 * [Network] handles and derive state from `set.isNotEmpty()` — so losing Wi-Fi while
 * LTE is still up does not flip the state to offline. The set is a
 * [ConcurrentHashMap.newKeySet] so reads and writes across the system callback thread
 * and any observer thread stay race-free.
 *
 * Registered once at app startup as a Koin singleton; the callback stays alive for the
 * process lifetime. [currentState] is a `MutableStateFlow`, keeping the synchronous
 * read (used by the HTTP pre-flight check) cheap and allocation-free.
 *
 * Requires `android.permission.ACCESS_NETWORK_STATE` in the manifest.
 */
@Singleton
internal class AndroidConnectivityMonitor(
    context: Context,
) : ConnectivityMonitor {

    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Thread-safe set of currently validated networks. The callback fires on a system
    // thread; state observers read from any thread. Publish to [state] after every
    // mutation so downstream sees a consistent derived value.
    private val validatedNetworks: MutableSet<Network> = ConcurrentHashMap.newKeySet()

    private val state = MutableStateFlow(readInitialState())

    private val callback = object : ConnectivityManager.NetworkCallback() {
        // Primary signal: a network matching our NetworkRequest (which includes
        // NET_CAPABILITY_VALIDATED) has come up. Add unconditionally — by the time
        // onAvailable fires, the system has already validated the network against
        // our request's capability filter.
        override fun onAvailable(network: Network) {
            validatedNetworks.add(network)
            publish()
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            // Capabilities can flip post-availability (e.g., captive portal detected
            // mid-session strips VALIDATED) — re-check and sync the set accordingly.
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
            // Terminal: the callback will not fire again. Clear and publish so the
            // pre-flight check sees Unavailable rather than a stale Available.
            validatedNetworks.clear()
            publish()
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        try {
            connectivityManager.registerNetworkCallback(request, callback)
        } catch (e: SecurityException) {
            // Most likely cause: missing `android.permission.ACCESS_NETWORK_STATE`.
            // Without the callback the monitor can only ever report the seeded
            // initial state, so every `isOnline()` check risks a false negative
            // (offline banner) or false positive (wasted HTTP attempts). Surface
            // the cause loudly — swallowing it silently has burned us before.
            Logger.e(e) {
                "Failed to register ConnectivityManager.NetworkCallback — connectivity " +
                    "state will be frozen at '${state.value}'. Verify ACCESS_NETWORK_STATE " +
                    "permission is declared in the manifest."
            }
        } catch (e: RuntimeException) {
            // TooManyRequestsException and similar unchecked throws at the system boundary.
            Logger.e(e) {
                "Unexpected failure registering ConnectivityManager.NetworkCallback — " +
                    "connectivity state will be frozen at '${state.value}'."
            }
        }
    }

    override val currentState: ConnectivityState
        get() = state.value

    override fun observe(): Flow<ConnectivityState> = state.asStateFlow()

    private fun publish() {
        state.value = if (validatedNetworks.isNotEmpty()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    }

    // Seed the state before the callback fires at least once. The callback will then
    // take over and the set becomes the source of truth.
    //
    // NOTE: this runs during property initialization — *before* the init block's
    // try/catch. A thrown SecurityException (missing ACCESS_NETWORK_STATE) or a
    // RuntimeException from the system service would therefore crash class
    // construction, making the app unusable before our init block's logging can
    // diagnose the cause. Catch both here and fall back to `Available` — treating
    // "we can't tell" as "assume online" is the less-bad UX than freezing every
    // `isOnline()` check to `false` (which would make the app appear permanently
    // offline). The root cause is still surfaced via Kermit for troubleshooting.
    private fun readInitialState(): ConnectivityState = try {
        val active = connectivityManager.activeNetwork ?: return ConnectivityState.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(active)
            ?: return ConnectivityState.Unavailable
        if (capabilities.isValidated()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    } catch (e: SecurityException) {
        Logger.e(e) {
            "Failed to read initial connectivity state — ACCESS_NETWORK_STATE permission " +
                "likely missing from the manifest. Falling back to 'Available' so requests " +
                "are not universally short-circuited as offline."
        }
        ConnectivityState.Available
    } catch (e: RuntimeException) {
        Logger.e(e) {
            "Unexpected failure reading initial connectivity state — " +
                "falling back to 'Available'."
        }
        ConnectivityState.Available
    }

    private fun NetworkCapabilities.isValidated(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
