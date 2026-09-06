package com.xbot.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey as Nav3Key
import org.jetbrains.compose.resources.StringResource

interface NavKey : Nav3Key {
    val requiresLogin: Boolean get() = false

    /**
     * Whether this destination replaces the app's top level navigation chrome rather than sitting
     * inside it — the player and the login flow do, an ordinary screen does not.
     *
     * The destination states the intent; making it happen is the chrome's business, and each does
     * it its own way — Compose collapses its navigation suite, iOS hides the native tab bar. The
     * decision itself is the same on every platform.
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
