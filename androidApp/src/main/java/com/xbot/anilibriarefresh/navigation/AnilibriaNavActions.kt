package com.xbot.anilibriarefresh.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Heart
import com.xbot.anilibriarefresh.icons.House
import com.xbot.anilibriarefresh.icons.Person
import com.xbot.anilibriarefresh.icons.Search
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route {
        @Serializable
        data object List

        @Serializable
        data class Detail(val titleId: Int)
    }

    @Serializable
    data object Favorite : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data class Player(val titleId: Int) : Route
}

enum class TopLevelDestination(
    @StringRes
    val textResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route,
) {
    Home(
        textResId = R.string.tab_home,
        selectedIcon = AnilibriaIcons.Filled.House,
        unselectedIcon = AnilibriaIcons.Outlined.House,
        route = Route.Home,
    ),
    Favorite(
        textResId = R.string.tab_favorite,
        selectedIcon = AnilibriaIcons.Filled.Heart,
        unselectedIcon = AnilibriaIcons.Outlined.Heart,
        route = Route.Favorite,
    ),
    Profile(
        textResId = R.string.tab_profile,
        selectedIcon = AnilibriaIcons.Outlined.Person,
        unselectedIcon = AnilibriaIcons.Outlined.Person,
        route = Route.Profile,
    ),
    ;

    companion object {
        val classNames = entries.map { it::route::class.qualifiedName }
    }
}

enum class NavigationContentPosition {
    TOP,
    CENTER,
}

enum class NavigationSuiteType {
    NavigationBar,
    NavigationRail,
}
