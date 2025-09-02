package com.xbot.home.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : Destination {
    override val isTopLevel: Boolean = true
}

fun NavGraphBuilder.homeSection(
    onSearchClick: NavBackStackEntry.() -> Unit,
    onReleaseClick: NavBackStackEntry.(Int) -> Unit
) {
    composable<HomeRoute> { navBackStackEntry ->
        HomeScreen(
            onSearchClick = {
                navBackStackEntry.onSearchClick()
            },
            onReleaseClick = { releaseId ->
                navBackStackEntry.onReleaseClick(releaseId)
            }
        )
    }
}
