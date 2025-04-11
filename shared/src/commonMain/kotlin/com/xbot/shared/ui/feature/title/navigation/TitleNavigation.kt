package com.xbot.shared.ui.feature.title.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xbot.shared.ui.feature.title.TitleScreen
import com.xbot.shared.ui.navigation.lifecycleIsResumed
import kotlinx.serialization.Serializable

@Serializable
data class TitleRoute(val aliasOrId: String)

fun NavHostController.navigateToTitle(id: Int) = navigate(TitleRoute(id.toString()))
fun NavHostController.navigateToTitle(alias: String) = navigate(TitleRoute(alias))

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