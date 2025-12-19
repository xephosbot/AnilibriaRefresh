package com.xbot.home.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Home
import com.xbot.resources.Res
import com.xbot.resources.tab_home
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object HomeRoute : TopLevelNavKey {
    override val textRes: StringResource
        get() = Res.string.tab_home
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Home
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Home
}

@Serializable
data object ScheduleRoute : NavKey
