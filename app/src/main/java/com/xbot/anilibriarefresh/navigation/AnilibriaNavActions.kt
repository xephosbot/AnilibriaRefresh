package com.xbot.anilibriarefresh.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
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
    val icon: ImageVector,
    val text: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        text = "Home",
        icon = Icons.Rounded.Home,
        route = Route.Home
    ),
    TopLevelDestination(
        text = "Favorite",
        icon = Icons.Rounded.Favorite,
        route = Route.Favorite
    ),
    TopLevelDestination(
        text = "Search",
        icon = Icons.Rounded.Search,
        route = Route.Search
    ),
    TopLevelDestination(
        text = "Profile",
        icon = Icons.Rounded.Person,
        route = Route.Profile
    )
)

enum class NavigationContentPosition {
    TOP, CENTER
}