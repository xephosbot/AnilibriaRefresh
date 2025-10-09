package com.xbot.common.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

interface NavKey : androidx.navigation3.runtime.NavKey

interface TopLevelNavKey : NavKey {
    val textRes: StringResource
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
}