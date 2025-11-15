package com.xbot.preference.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Settings
import com.xbot.resources.Res
import com.xbot.resources.tab_preference
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object PreferenceRoute : TopLevelNavKey {
    override val textRes: StringResource
        get() = Res.string.tab_preference
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Settings
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Settings
}

data object PreferenceProfileRoute : NavKey

data object PreferenceHistoryRoute : NavKey

data object PreferenceTeamRoute : NavKey

data object PreferenceDonateRoute : NavKey

data object PreferenceSettingsRoute : NavKey
