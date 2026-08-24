package com.xbot.favorite.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.navigation.TopLevelNavKey
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.Favorite
import com.xbot.resources.Res
import com.xbot.resources.tab_favorite
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object FavoriteRoute : TopLevelNavKey {
    override val requiresLogin: Boolean
        get() = true
    override val textRes: StringResource
        get() = Res.string.tab_favorite
    override val selectedIcon: ImageVector
        get() = AnilibertyIcons.Filled.Favorite
    override val unselectedIcon: ImageVector
        get() = AnilibertyIcons.Outlined.Favorite
}
