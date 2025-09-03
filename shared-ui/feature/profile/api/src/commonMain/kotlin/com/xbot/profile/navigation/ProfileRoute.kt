package com.xbot.profile.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.common.navigation.TopLevelDestination
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.resources.Res
import com.xbot.resources.tab_profile
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object ProfileRoute : TopLevelDestination {
    override val textRes: StringResource
        get() = Res.string.tab_profile
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Person
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Person

}