package com.xbot.favorite.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.TopLevelDestination
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.favorite.FavoriteScreen
import com.xbot.resources.Res
import com.xbot.resources.tab_favorite
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object FavoriteRoute : TopLevelDestination {
    override val textRes: StringResource
        get() = Res.string.tab_favorite
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Heart
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Heart
}

fun NavGraphBuilder.favoriteSection() {
    composable<FavoriteRoute> {
        FavoriteScreen()
    }
}