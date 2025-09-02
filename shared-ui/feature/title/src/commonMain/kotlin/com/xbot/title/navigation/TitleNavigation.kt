package com.xbot.title.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.title.TitleScreen
import kotlinx.serialization.Serializable

@Serializable
data class TitleRoute(val aliasOrId: String) : Destination

fun NavGraphBuilder.titleScreen(
    onBackClick: NavBackStackEntry.() -> Unit,
    onPlayClick: NavBackStackEntry.(releaseId: Int, episodeOrdinal: Int) -> Unit,
    onReleaseClick: NavBackStackEntry.(Int) -> Unit
) {
    composable<TitleRoute> { navBackStackEntry ->
        TitleScreen(
            onBackClick = {
                navBackStackEntry.onBackClick()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                navBackStackEntry.onPlayClick(releaseId, episodeOrdinal)
            },
            onReleaseClick = { releaseId ->
                navBackStackEntry.onReleaseClick(releaseId)
            }
        )
    }
}