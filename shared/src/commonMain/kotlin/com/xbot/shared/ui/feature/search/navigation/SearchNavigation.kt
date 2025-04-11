package com.xbot.shared.ui.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xbot.shared.ui.feature.search.SearchScreen
import com.xbot.shared.ui.navigation.lifecycleIsResumed
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