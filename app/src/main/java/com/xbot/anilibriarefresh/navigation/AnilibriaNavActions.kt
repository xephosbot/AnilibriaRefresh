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
        data class Detail(val titleId: Int)
    }

    @Serializable
    data object Favorite : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Profile : Route
}

data class TopLevelDestination(
    val route: Route,
    val iconSelected: ImageVector,
    val iconDeselected: ImageVector,
    val text: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        text = "Home",
        iconSelected = AnilibriaIcons.Filled.House,
        iconDeselected = AnilibriaIcons.Outlined.House,
        route = Route.Home
    ),
    TopLevelDestination(
        text = "Favorite",
        iconSelected = AnilibriaIcons.Filled.Heart,
        iconDeselected = AnilibriaIcons.Outlined.Heart,
        route = Route.Favorite
    ),
    TopLevelDestination(
        text = "Search",
        iconSelected = AnilibriaIcons.Outlined.Search,
        iconDeselected = AnilibriaIcons.Outlined.Search,
        route = Route.Search
    ),
    TopLevelDestination(
        text = "Profile",
        iconSelected = AnilibriaIcons.Outlined.Person,
        iconDeselected = AnilibriaIcons.Outlined.Person,
        route = Route.Profile
    )
)

enum class NavigationContentPosition {
    TOP, CENTER
}