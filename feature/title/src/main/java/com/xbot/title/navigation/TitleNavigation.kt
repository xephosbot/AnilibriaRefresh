package com.xbot.title.navigation

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.xbot.api.AnilibriaApi
import com.xbot.common.navigation.lifecycleIsResumed
import com.xbot.title.TitleScreen
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
    composable<TitleRoute>(
        deepLinks = listOf(
            navDeepLink<TitleRoute>(
                basePath = AnilibriaApi.withBaseUrl("/anime/releases/release")
            ) {
                action = Intent.ACTION_VIEW
            }
        )
    ) { navBackStackEntry ->
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