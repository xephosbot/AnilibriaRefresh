package com.xbot.anilibriarefresh.navigation

import com.xbot.anilibriarefresh.R
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
    val iconCurrent: Int,
    val iconInactive: Int,
    val text: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        text = "Home",
        iconCurrent = R.drawable.icon_home_filled,
        iconInactive = R.drawable.icon_home_outlined,
        route = Route.Home
    ),
    TopLevelDestination(
        text = "Favorite",
        iconCurrent = R.drawable.icon_heart_filled,
        iconInactive = R.drawable.icon_favorites_outlined,
        route = Route.Favorite
    ),
    TopLevelDestination(
        text = "Search",
        iconCurrent = R.drawable.icon_search_filled,
        iconInactive = R.drawable.icon_search_outlined,
        route = Route.Search
    ),
    TopLevelDestination(
        text = "Profile",
        iconCurrent = R.drawable.icon_person_filled,
        iconInactive = R.drawable.icon_person_outlined,
        route = Route.Profile
    )
)

enum class NavigationContentPosition {
    TOP, CENTER
}