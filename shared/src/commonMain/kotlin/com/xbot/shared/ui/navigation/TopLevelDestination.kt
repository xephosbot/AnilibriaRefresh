package com.xbot.shared.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.shared.resources.Res
import com.xbot.shared.resources.tab_home
import com.xbot.shared.resources.tab_favorite
import com.xbot.shared.resources.tab_profile
import com.xbot.shared.ui.designsystem.icons.AnilibriaIcons
import com.xbot.shared.ui.feature.favorite.navigation.FavoriteRoute
import com.xbot.shared.ui.feature.home.navigation.HomeRoute
import com.xbot.shared.ui.feature.profile.navigation.ProfileRoute
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
