package com.xbot.sharedapp.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.profile.navigation.ProfileRoute
import com.xbot.resources.Res
import com.xbot.resources.tab_favorite
import com.xbot.resources.tab_home
import com.xbot.resources.tab_profile
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(
    val textRes: StringResource,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any,
) {
    Home(
        textRes = Res.string.tab_home,
        selectedIcon = AnilibriaIcons.Filled.Home,
        unselectedIcon = AnilibriaIcons.Outlined.Home,
        route = HomeRoute,
    ),
    Favorite(
        textRes = Res.string.tab_favorite,
        selectedIcon = AnilibriaIcons.Filled.Heart,
        unselectedIcon = AnilibriaIcons.Outlined.Heart,
        route = FavoriteRoute,
    ),
    Profile(
        textRes = Res.string.tab_profile,
        selectedIcon = AnilibriaIcons.Filled.Person,
        unselectedIcon = AnilibriaIcons.Outlined.Person,
        route = ProfileRoute,
    );
}
