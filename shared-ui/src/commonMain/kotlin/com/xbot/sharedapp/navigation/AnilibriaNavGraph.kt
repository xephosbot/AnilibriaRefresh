package com.xbot.sharedapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.xbot.favorite.navigation.favoriteSection
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.homeSection
import com.xbot.player.navigation.PlayerRoute
import com.xbot.player.navigation.playerScreen
import com.xbot.profile.navigation.profileSection
import com.xbot.search.navigation.SearchRoute
import com.xbot.search.navigation.searchScreen
import com.xbot.sharedapp.AnilibriaNavigator
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.titleScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
internal fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navigator: AnilibriaNavigator,
    startDestination: Any = HomeRoute,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navigator.navController,
        startDestination = startDestination,
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        homeSection(
            onSearchClick = {
                if (lifecycleIsResumed()) navigator.navigate(SearchRoute)
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navigator.navigate(TitleRoute(releaseId.toString()))
            }
        )
        favoriteSection()
        profileSection(
            onReleaseClick = { releaseId ->
                navigator.navigate(TitleRoute(releaseId.toString()))
            }
        )

        titleScreen(
            onBackClick = {
                if (lifecycleIsResumed()) navigator.navigateBack()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                if (lifecycleIsResumed()) navigator.navigate(PlayerRoute(releaseId, episodeOrdinal))
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navigator.navigate(TitleRoute(releaseId.toString()))
            }
        )
        searchScreen(
            onBackClick = {
                if (lifecycleIsResumed()) navigator.navigateBack()
            },
            onReleaseClick = { releaseId ->
                if (lifecycleIsResumed()) navigator.navigate(TitleRoute(releaseId.toString()))
            }
        )
        playerScreen(
            onBackClick = {
                navigator.navigateBack()
            }
        )
    }
}
