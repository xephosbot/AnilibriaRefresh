package com.xbot.search.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.search.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute : Destination

fun NavGraphBuilder.searchScreen(
    onBackClick: NavBackStackEntry.() -> Unit,
    onReleaseClick: NavBackStackEntry.(Int) -> Unit
) {
    composable<SearchRoute> { navBackStackEntry ->
        SearchScreen(
            onBackClick = {
                navBackStackEntry.onBackClick()
            },
            onReleaseClick = { releaseId ->
                navBackStackEntry.onReleaseClick(releaseId)
            }
        )
    }
}