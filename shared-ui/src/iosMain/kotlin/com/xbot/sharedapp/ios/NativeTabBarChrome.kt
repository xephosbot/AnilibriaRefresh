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

@Immutable
internal data class NativeTabBarState(
    val topLevelRoute: TopLevelNavKey?,
    val tabBarVisible: Boolean,
    val tabTitles: Map<TopLevelNavKey, String>,
    val themeOption: ThemeOption,
)

internal class NativeTabBarChrome(
    private val controller: AnilibertyTabBarController,
) : NavigationChrome {

    @Composable
    override fun Content(navigator: Navigator, content: @Composable () -> Unit) {
        TabBarProjection(navigator)
        content()
    }

    @Composable
    private fun TabBarProjection(navigator: Navigator) {
        DisposableEffect(navigator) {
            controller.onTabSelected = { destination -> navigator.navigate(destination) }
            onDispose { controller.onTabSelected = null }
        }

        // Resolved inside the composition, so labels and appearance follow a language or theme
        // change for free; a one-shot getString from outside it would see neither.
        val state = NativeTabBarState(
            topLevelRoute = navigator.currentTopLevelDestination,
            tabBarVisible = navigator.currentDestination?.hidesNavigationBar != true,
            tabTitles = TopLevelRoutes.associateWith { stringResource(it.textRes) },
            themeOption = LocalAppState.current.themeState.themeOption,
        )
        SideEffect { controller.applyState(state) }
    }
}
