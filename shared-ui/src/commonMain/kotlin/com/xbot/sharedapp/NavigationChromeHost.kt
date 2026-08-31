package com.xbot.sharedapp

import com.xbot.navigation.TopLevelNavKey

/**
 * Bridge between the Compose navigation state and platform-owned navigation chrome.
 *
 * The contract is deliberately one-way: Navigation 3 stays the single source of truth and pushes
 * its state out through [onNavigationStateChanged], while the platform chrome only ever reports
 * user intent back through [onTabSelected]. Nothing is mirrored, so there is no state to keep in
 * sync in either direction.
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

    /**
     * Set from the composition; invoked by the platform chrome when the user picks a destination.
     */
    var onTabSelected: ((TopLevelNavKey) -> Unit)?
}
