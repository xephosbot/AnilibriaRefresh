package com.xbot.anilibriarefresh.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.icons.House
import com.xbot.anilibriarefresh.ui.icons.Person
import com.xbot.anilibriarefresh.ui.icons.Search
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route {
        @Serializable
        data object List

        @Serializable
        data class Detail(
            val titleId: Int,
            val titleName: String
        )
    }

    @Serializable
    data object Favorite : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object Player : Route
}

//TODO: move string to resources
enum class TopLevelDestination(
    val text: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route
) {
    Home(
        text = "Home",
        selectedIcon = AnilibriaIcons.Filled.House,
        unselectedIcon = AnilibriaIcons.Outlined.House,
        route = Route.Home
    ),
    Favorite(
        text = "Favorite",
        selectedIcon = AnilibriaIcons.Filled.Heart,
        unselectedIcon = AnilibriaIcons.Outlined.Heart,
        route = Route.Favorite
    ),
    Search(
        text = "Search",
        selectedIcon = AnilibriaIcons.Outlined.Search,
        unselectedIcon = AnilibriaIcons.Outlined.Search,
        route = Route.Search
    ),
    Profile(
        text = "Profile",
        selectedIcon = AnilibriaIcons.Outlined.Person,
        unselectedIcon = AnilibriaIcons.Outlined.Person,
        route = Route.Profile
    );

    companion object {
        val classNames = entries.map { it::route::class.qualifiedName }
    }
}

enum class NavigationContentPosition {
    TOP, CENTER
}

enum class NavigationSuiteType {
    NavigationBar, NavigationRail
}