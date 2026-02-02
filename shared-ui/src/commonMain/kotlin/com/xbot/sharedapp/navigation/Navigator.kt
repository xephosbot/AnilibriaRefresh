package com.xbot.sharedapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.sharedapp.navigation.deeplink.DeepLinkListener
import com.xbot.sharedapp.navigation.deeplink.ExternalUriHandler
import kotlinx.serialization.modules.SerializersModule

@Composable
fun rememberNavigator(
    startRoute: TopLevelNavKey,
    topLevelRoutes: Set<TopLevelNavKey>,
    serializersModule: SerializersModule,
    onInterceptNavigation: (NavKey) -> NavKey = { it },
): Navigator {
    val navigationState = rememberNavigationState(startRoute, topLevelRoutes, serializersModule)
    val currentOnInterceptNavigation by rememberUpdatedState(onInterceptNavigation)

    val navigator = remember(navigationState) {
        AnilibriaNavigator(
            state = navigationState,
            navigationInterceptor = { key -> currentOnInterceptNavigation(key) }
        )
    }

    DeepLinkListener { key ->
        navigator.navigate(key)
    }

    return navigator
}

internal class AnilibriaNavigator(
    val state: NavigationState,
    val navigationInterceptor: (NavKey) -> NavKey,
) : Navigator {

    override val currentTopLevelDestination: TopLevelNavKey
        get() = state.topLevelRoute

    override val backStack: List<NavKey>
        get() = state.topLevelRoutesInUse.flatMap { state.backStacks[it] ?: emptyList() }

    override val currentDestination: NavKey?
        get() = state.backStacks[state.topLevelRoute]?.lastOrNull()

    override fun navigate(key: NavKey) {
        val targetKey = navigationInterceptor(key)

        if (targetKey is ExternalLinkNavKey) {
            ExternalUriHandler.onNewUri(targetKey.url)
            return
        }

        when (targetKey) {
            is TopLevelNavKey -> {
                state.topLevelRoute = targetKey
            }
            else -> {
                state.backStacks[state.topLevelRoute]?.add(targetKey)
            }
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
