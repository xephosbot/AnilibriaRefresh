package com.xbot.preference.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.resources.Res
import com.xbot.resources.tab_profile
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object PreferenceRoute : TopLevelNavKey {
    override val textRes: StringResource
        get() = Res.string.tab_profile
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Person
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Person
}

data object PreferenceProfileRoute : NavKey

data object PreferenceHistoryRoute : NavKey

data object PreferenceTeamRoute : NavKey

data object PreferenceDonateRoute : NavKey

data object PreferenceSettingsRoute : NavKey
