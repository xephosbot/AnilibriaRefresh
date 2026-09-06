package com.xbot.sharedapp.ios

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import com.xbot.common.state.LocalAppState
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.navigation.Navigator
import com.xbot.navigation.TopLevelNavKey
import com.xbot.navigation.TopLevelRoutes
import com.xbot.sharedapp.navigation.NavigationChrome
import org.jetbrains.compose.resources.stringResource

/**
 * Everything the native tab bar needs in order to mirror the app.
 *
 * A single snapshot rather than one callback per property: the bar is a pure projection of Compose
 * state, so there is nothing to reconcile field by field, and pushing the whole thing keeps it
 * obvious that the platform side holds no state of its own.
 */
@Immutable
internal data class NativeTabBarState(
    val topLevelRoute: TopLevelNavKey?,
    val tabBarVisible: Boolean,
    val tabTitles: Map<TopLevelNavKey, String>,
    val themeOption: ThemeOption,
)

/**
 * The iOS navigation chrome: renders nothing, and instead projects the composition onto the
 * [AnilibertyTabBarController] that hosts it.
 *
 * The contract is deliberately one-way. Navigation 3 stays the single source of truth and pushes
 * snapshots down into the controller; the bar only ever reports user intent back, never its own
 * state. Nothing is mirrored, so there is nothing to keep in sync in either direction.
 */
internal class NativeTabBarChrome(
    private val controller: AnilibertyTabBarController,
) : NavigationChrome {

    @Composable
    override fun Content(navigator: Navigator, content: @Composable () -> Unit) {
        // Kept out of the caller's recompose scope so that reading navigation, locale and theme
        // state here does not drag the whole navigation graph through recomposition with it.
        TabBarProjection(navigator)

        content()
    }

    @Composable
    private fun TabBarProjection(navigator: Navigator) {
        DisposableEffect(navigator) {
            controller.onTabSelected = { destination -> navigator.navigate(destination) }
            onDispose { controller.onTabSelected = null }
        }

        // Resolved from inside the composition on purpose: NavigationChrome.Content runs within
        // ProvideAppLocale and AnilibertyTheme, which is what makes the native bar follow a
        // language or theme change for free. A one-shot getString from iosMain would see neither.
        val state = NativeTabBarState(
            topLevelRoute = navigator.currentTopLevelDestination,
            tabBarVisible = navigator.currentDestination?.hidesNavigationBar != true,
            tabTitles = TopLevelRoutes.associateWith { stringResource(it.textRes) },
            themeOption = LocalAppState.current.themeState.themeOption,
        )

        // SideEffect, not LaunchedEffect: applying a snapshot is a synchronous UIKit call that
        // should land with the frame that produced it, and the controller ignores anything that
        // has not actually changed.
        SideEffect { controller.applyState(state) }
    }
}
