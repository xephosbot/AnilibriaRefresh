package com.xbot.title.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xbot.common.navigation.lifecycleIsResumed
import com.xbot.title.TitleScreen
import kotlinx.serialization.Serializable

@Serializable
data class TitleRoute(val id: Int)

fun NavHostController.navigateToTitle(id: Int) = navigate(TitleRoute(id))

fun NavGraphBuilder.titleScreen(
    onBackClick: () -> Unit,
    onPlayClick: (releaseId: Int, episodeOrdinal: Int) -> Unit,
    onReleaseClick: (Int) -> Unit
) {
    composable<TitleRoute> { navBackStackEntry ->
        TitleScreen(
            onBackClick = {
                if (navBackStackEntry.lifecycleIsResumed()) onBackClick()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                if (navBackStackEntry.lifecycleIsResumed()) onPlayClick(releaseId, episodeOrdinal)
            },
            onReleaseClick = { releaseId ->
                if (navBackStackEntry.lifecycleIsResumed()) onReleaseClick(releaseId)
            }
        )
    }
}