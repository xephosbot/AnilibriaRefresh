package com.xbot.anilibriarefresh.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.anilibriarefresh.R
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.profile.navigation.ProfileRoute
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class Player(val titleId: Int)
}

enum class TopLevelDestination(
    @StringRes
    val textResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any,
) {
    Home(
        textResId = R.string.tab_home,
        selectedIcon = AnilibriaIcons.Filled.Home,
        unselectedIcon = AnilibriaIcons.Outlined.Home,
        route = HomeRoute,
    ),
    Favorite(
        textResId = R.string.tab_favorite,
        selectedIcon = AnilibriaIcons.Filled.Heart,
        unselectedIcon = AnilibriaIcons.Outlined.Heart,
        route = FavoriteRoute,
    ),
    Profile(
        textResId = R.string.tab_profile,
        selectedIcon = AnilibriaIcons.Filled.Person,
        unselectedIcon = AnilibriaIcons.Outlined.Person,
        route = ProfileRoute,
    ),
    ;

    companion object {
        val classNames = entries.map { it::route::class.qualifiedName }
    }
}
