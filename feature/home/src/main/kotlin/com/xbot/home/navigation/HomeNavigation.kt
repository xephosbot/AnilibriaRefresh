package com.xbot.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.lifecycleIsResumed
import com.xbot.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeSection(
    onSearchClick: () -> Unit,
    onReleaseClick: (Int) -> Unit
) {
    composable<HomeRoute> { navBackStackEntry ->
        HomeScreen(
            onSearchClick = {
                if (navBackStackEntry.lifecycleIsResumed()) onSearchClick()
            },
            onReleaseClick = { releaseId ->
                if (navBackStackEntry.lifecycleIsResumed()) onReleaseClick(releaseId)
            }
        )
    }
}
