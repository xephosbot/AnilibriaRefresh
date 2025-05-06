package com.xbot.sharedapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.xbot.favorite.navigation.favoriteSection
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.homeSection
import com.xbot.home.navigation.navigateToSchedule
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.player.navigation.playerScreen
import com.xbot.profile.navigation.profileSection
import com.xbot.search.navigation.navigateToSearch
import com.xbot.search.navigation.searchScreen
import com.xbot.sharedapp.AnilibriaAppState
import com.xbot.title.navigation.navigateToTitle
import com.xbot.title.navigation.titleScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    appState: AnilibriaAppState,
    startDestination: Any = HomeRoute,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        homeSection(
            onBackClick = {
                if (lifecycleIsResumed()) navController.navigateUp()
            },
            onSearchClick = {
                if (lifecycleIsResumed()) navController.navigateToSearch()
            },
            onScheduleClick = {
                if (lifecycleIsResumed()) navController.navigateToSchedule()
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navController.navigateToTitle(releaseId)
            }
        )
        favoriteSection()
        profileSection()

        titleScreen(
            onBackClick = {
                if (lifecycleIsResumed()) navController.navigateUp()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                if (lifecycleIsResumed()) navController.navigateToPlayer(releaseId, episodeOrdinal)
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navController.navigateToTitle(releaseId)
            }
        )
        searchScreen(
            onBackClick = {
                if (lifecycleIsResumed())  navController.navigateUp()
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navController.navigateToTitle(releaseId)
            }
        )
        playerScreen(
            onBackClick = {
                navController.navigateUp()
            }
        )
    }
}
