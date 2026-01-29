package com.xbot.sharedapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.login.navigation.LoginRoute
import com.xbot.sharedapp.navigation.deeplink.DeepLinkListener
import com.xbot.sharedapp.navigation.deeplink.parseDeepLink
import kotlinx.serialization.modules.SerializersModule

@Composable
fun rememberNavigator(
    startRoute: TopLevelNavKey,
    topLevelRoutes: Set<TopLevelNavKey>,
    serializersModule: SerializersModule,
): Navigator {
    val navigationState = rememberNavigationState(startRoute, topLevelRoutes, serializersModule)
    val uriHandler = LocalUriHandler.current

    val navigator = remember(navigationState, uriHandler) {
        AnilibriaNavigator(
            state = navigationState,
            onNavigateToRestrictedKey = { _ -> LoginRoute },
            uriHandler = uriHandler
        )
    }

    DeepLinkListener { uri ->
        navigator.handleDeepLink(uri)
    }

    return navigator
}

internal class AnilibriaNavigator(
    val state: NavigationState,
    val onNavigateToRestrictedKey: (targetKey: NavKey?) -> NavKey,
    val uriHandler: UriHandler,
) : Navigator {
    override val currentTopLevelDestination: TopLevelNavKey
        get() = state.topLevelRoute

    override val backStack: List<NavKey>
        get() = state.topLevelRoutesInUse.flatMap { state.backStacks[it] ?: emptyList() }

    override val currentDestination: NavKey?
        get() = state.backStacks[state.topLevelRoute]?.lastOrNull()

    override fun navigate(key: NavKey) {
        if (key.requiresLogin) {
            navigate(onNavigateToRestrictedKey(key))
            return
        }

        if (key is ExternalLinkNavKey) {
            uriHandler.openUri(key.url)
            return
        }

        when (key) {
            is TopLevelNavKey -> {
                state.topLevelRoute = key
            }
            else -> {
                state.backStacks[state.topLevelRoute]?.add(key)
            }
        }
    }

    fun handleDeepLink(uri: String) {
        val key = parseDeepLink(uri)
        if (key != null) {
            navigate(key)
        }
    }

    override fun navigateBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")

        val currentRoute = currentStack.lastOrNull() ?: return

        if (currentRoute == state.topLevelRoute) {
            if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
            }
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
