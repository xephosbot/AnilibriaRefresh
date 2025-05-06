package com.xbot.home.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xbot.home.HomeScreen
import com.xbot.home.ScheduleScreen
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
    onBackClick: NavBackStackEntry.() -> Unit,
    onSearchClick: NavBackStackEntry.() -> Unit,
    onScheduleClick: NavBackStackEntry.() -> Unit,
    onReleaseClick: NavBackStackEntry.(Int) -> Unit
) {
    navigation<HomeRoute>(HomeRoute.Feed) {
        composable<HomeRoute.Feed> { navBackStackEntry ->
            HomeScreen(
                onSearchClick = {
                    navBackStackEntry.onSearchClick()
                },
                onScheduleClick = {
                    navBackStackEntry.onScheduleClick()
                },
                onReleaseClick = { releaseId ->
                    navBackStackEntry.onReleaseClick(releaseId)
                }
            )
        }
        composable<HomeRoute.Schedule> { navBackStackEntry ->
            ScheduleScreen(
                onBackClick = {
                    navBackStackEntry.onBackClick()
                },
                onReleaseClick = { releaseId ->
                    navBackStackEntry.onReleaseClick(releaseId)
                }
            )
        }
    }
}
