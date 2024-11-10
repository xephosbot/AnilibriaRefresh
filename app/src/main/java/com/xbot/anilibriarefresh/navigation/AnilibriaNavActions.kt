package com.xbot.anilibriarefresh.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.xbot.anilibriarefresh.R
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
            val titleName: String,
        )
    }

    @Serializable
    data object Favorite : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data class Player(val titleId: Int) : Route
}

enum class TopLevelDestination(
    val text: @Composable () -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route,
) {
    Home(
        text = { stringResource(R.string.home) },
        selectedIcon = AnilibriaIcons.Filled.House,
        unselectedIcon = AnilibriaIcons.Outlined.House,
        route = Route.Home,
    ),
    Favorite(
        text = { stringResource(R.string.favorite) },
        selectedIcon = AnilibriaIcons.Filled.Heart,
        unselectedIcon = AnilibriaIcons.Outlined.Heart,
        route = Route.Favorite,
    ),
    Search(
        text = { stringResource(R.string.search) },
        selectedIcon = AnilibriaIcons.Outlined.Search,
        unselectedIcon = AnilibriaIcons.Outlined.Search,
        route = Route.Search,
    ),
    Profile(
        text = { stringResource(R.string.profile) },
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
