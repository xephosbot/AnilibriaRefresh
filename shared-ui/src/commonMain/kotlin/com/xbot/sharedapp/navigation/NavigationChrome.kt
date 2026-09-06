package com.xbot.sharedapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.xbot.navigation.Navigator

/** Top level navigation chrome. Compose renders its own; iOS drives a native tab bar instead. */
@Stable
internal interface NavigationChrome {

    @Composable
    fun Content(navigator: Navigator, content: @Composable () -> Unit)
}

internal val LocalNavigationChrome: ProvidableCompositionLocal<NavigationChrome> =
    staticCompositionLocalOf { ComposeNavigationChrome }
