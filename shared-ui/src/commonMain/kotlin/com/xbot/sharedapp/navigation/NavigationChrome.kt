package com.xbot.sharedapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.xbot.navigation.Navigator

/**
 * The app's top level navigation chrome — whatever lets the user move between the top level
 * destinations.
 *
 * Which implementation is in play is a property of the platform, not a decision the composition
 * makes: [ComposeNavigationChrome] draws a `NavigationSuiteScaffold`, while iOS installs one that
 * draws nothing and projects the same state onto a native `UITabBarController` instead. Both wrap
 * the navigation graph, and neither owns navigation state — Navigation 3 stays the single source of
 * truth, and a chrome only ever reads from [Navigator] and reports taps back into it.
 *
 * [Content] runs inside the app's theme and locale, so an implementation may resolve string
 * resources and read `LocalAppState`; that is what lets even a platform-owned bar follow a language
 * or theme change without anything having to notify it.
 */
@Stable
internal interface NavigationChrome {

    @Composable
    fun Content(navigator: Navigator, content: @Composable () -> Unit)
}

/**
 * The chrome this platform uses. Static, because no platform swaps its chrome mid-composition and
 * making it observable would only buy an invalidation nobody triggers.
 */
internal val LocalNavigationChrome: ProvidableCompositionLocal<NavigationChrome> =
    staticCompositionLocalOf { ComposeNavigationChrome }
