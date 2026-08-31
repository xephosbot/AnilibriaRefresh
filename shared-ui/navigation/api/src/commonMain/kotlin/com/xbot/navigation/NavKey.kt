package com.xbot.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey as Nav3Key
import org.jetbrains.compose.resources.StringResource

interface NavKey : Nav3Key {
    val requiresLogin: Boolean get() = false

    /**
     * Whether this destination covers the platform navigation chrome.
     *
     * On iOS the tab bar is a native `UITabBarController` drawn above the Compose
     * scene, so full screen destinations must ask for it to be hidden explicitly. On platforms
     * where the chrome is rendered by Compose itself this flag is unused.
     */
    val hidesNavigationBar: Boolean get() = false
}

interface TopLevelNavKey : NavKey {
    val textRes: StringResource
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
}

interface ExternalUriNavKey : NavKey {
    val uri: String
}
