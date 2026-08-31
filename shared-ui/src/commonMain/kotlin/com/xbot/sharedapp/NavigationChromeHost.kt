package com.xbot.sharedapp

import com.xbot.domain.models.enums.ThemeOption
import com.xbot.navigation.TopLevelNavKey

/**
 * Bridge between the Compose navigation state and platform-owned navigation chrome.
 *
 * The contract is deliberately one-way: Navigation 3 stays the single source of truth and pushes
 * its state out through the `on*Changed` callbacks, while the platform chrome only ever reports
 * user intent back through [onTabSelected]. Nothing is mirrored, so there is no state to keep in
 * sync in either direction.
 *
 * Every push originates from the composition, so anything the chrome mirrors — labels, appearance —
 * follows a locale or theme change for free.
 *
 * When no host is supplied, the app renders its own Compose navigation chrome instead.
 */
internal interface NavigationChromeHost {

    /**
     * Invoked from the composition whenever the navigation state changes.
     *
     * @param topLevel the currently selected top level destination, or `null` before one resolves.
     * @param chromeVisible `false` while a destination that covers the chrome is on top.
     */
    fun onNavigationStateChanged(topLevel: TopLevelNavKey?, chromeVisible: Boolean)

    /** Localized labels for the top level destinations, re-pushed on every language change. */
    fun onTabTitlesChanged(titles: Map<TopLevelNavKey, String>)

    /**
     * The app's theme preference.
     *
     * Deliberately the raw [ThemeOption] rather than a resolved "is dark" flag: the platform chrome
     * must leave its appearance unspecified for [ThemeOption.System], otherwise overriding it would
     * also pin the trait collection Compose reads and the app would stop following the system.
     */
    fun onThemeOptionChanged(themeOption: ThemeOption)

    /**
     * Set from the composition; invoked by the platform chrome when the user picks a destination.
     */
    var onTabSelected: ((TopLevelNavKey) -> Unit)?
}
