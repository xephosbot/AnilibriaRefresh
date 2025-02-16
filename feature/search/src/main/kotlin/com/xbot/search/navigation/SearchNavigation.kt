package com.xbot.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xbot.common.navigation.lifecycleIsResumed
import com.xbot.search.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute

fun NavHostController.navigateToSearch() = navigate(SearchRoute)

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onReleaseClick: (Int) -> Unit
) {
    composable<SearchRoute> { navBackStackEntry ->
        SearchScreen(
            onBackClick = {
                if (navBackStackEntry.lifecycleIsResumed()) onBackClick()
            },
            onReleaseClick = { releaseId ->
                if (navBackStackEntry.lifecycleIsResumed()) onReleaseClick(releaseId)
            }
        )
    }
}