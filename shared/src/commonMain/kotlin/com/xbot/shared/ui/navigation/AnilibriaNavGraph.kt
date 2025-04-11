package com.xbot.shared.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.xbot.shared.ui.AnilibriaAppState
import com.xbot.shared.ui.feature.favorite.navigation.favoriteSection
import com.xbot.shared.ui.feature.home.navigation.HomeRoute
import com.xbot.shared.ui.feature.home.navigation.homeSection
import com.xbot.shared.ui.feature.home.navigation.navigateToSchedule
import com.xbot.shared.ui.feature.player.navigation.navigateToPlayer
import com.xbot.shared.ui.feature.player.navigation.playerScreen
import com.xbot.shared.ui.feature.profile.navigation.profileSection
import com.xbot.shared.ui.feature.search.navigation.navigateToSearch
import com.xbot.shared.ui.feature.search.navigation.searchScreen
import com.xbot.shared.ui.feature.title.navigation.navigateToTitle
import com.xbot.shared.ui.feature.title.navigation.titleScreen
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
                navController.navigateUp()
            },
            onSearchClick = {
                navController.navigateToSearch()
            },
            onScheduleClick = {
                navController.navigateToSchedule()
            },
            onReleaseClick = { releaseId ->
                navController.navigateToTitle(releaseId)
            }
        )
        favoriteSection()
        profileSection()

        titleScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                navController.navigateToPlayer(releaseId, episodeOrdinal)
            },
            onReleaseClick = { releaseId ->
                navController.navigateToTitle(releaseId)
            }
        )
        searchScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onReleaseClick = { releaseId ->
                navController.navigateToTitle(releaseId)
            }
        )
        playerScreen()
    }
}
