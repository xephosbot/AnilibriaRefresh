package com.xbot.common.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey as Nav3Key
import org.jetbrains.compose.resources.StringResource

interface NavKey : Nav3Key {
    val requiresLogin: Boolean get() = false
}

interface TopLevelNavKey : NavKey {
    val textRes: StringResource
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
}

interface ExternalUriNavKey : NavKey {
    val uri: String
}
