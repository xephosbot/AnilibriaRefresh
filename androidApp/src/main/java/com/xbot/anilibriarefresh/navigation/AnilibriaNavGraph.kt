package com.xbot.anilibriarefresh.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.xbot.anilibriarefresh.ui.AnilibriaAppState
import com.xbot.favorite.navigation.favoriteSection
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.homeSection
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.player.navigation.playerScreen
import com.xbot.profile.navigation.profileSection
import com.xbot.search.navigation.navigateToSearch
import com.xbot.search.navigation.searchScreen
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
            onSearchClick = {
                navController.navigateToSearch()
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
