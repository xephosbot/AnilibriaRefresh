package com.xbot.shared.ui.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xbot.shared.ui.feature.home.HomeScreen
import com.xbot.shared.ui.feature.home.ScheduleScreen
import com.xbot.shared.ui.navigation.lifecycleIsResumed
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute {
    @Serializable
    data object Feed

    @Serializable
    data object Schedule
}

fun NavHostController.navigateToSchedule() = navigate(HomeRoute.Schedule)

fun NavGraphBuilder.homeSection(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onReleaseClick: (Int) -> Unit
) {
    navigation<HomeRoute>(HomeRoute.Feed) {
        composable<HomeRoute.Feed> { navBackStackEntry ->
            HomeScreen(
                onSearchClick = {
                    if (navBackStackEntry.lifecycleIsResumed()) onSearchClick()
                },
                onScheduleClick = {
                    if (navBackStackEntry.lifecycleIsResumed()) onScheduleClick()
                },
                onReleaseClick = { releaseId ->
                    if (navBackStackEntry.lifecycleIsResumed()) onReleaseClick(releaseId)
                }
            )
        }
        composable<HomeRoute.Schedule> { navBackStackEntry ->
            ScheduleScreen(
                onBackClick = {
                    if (navBackStackEntry.lifecycleIsResumed()) onBackClick()
                },
                onReleaseClick = { releaseId ->
                    if (navBackStackEntry.lifecycleIsResumed()) onReleaseClick(releaseId)
                }
            )
        }
    }
}
