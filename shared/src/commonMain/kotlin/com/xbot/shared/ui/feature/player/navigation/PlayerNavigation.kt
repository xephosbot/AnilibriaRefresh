package com.xbot.shared.ui.feature.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(val id: Int, val episodeOrdinal: Int)

fun NavHostController.navigateToPlayer(id: Int, episodeOrdinal: Int) = navigate(PlayerRoute(id, episodeOrdinal))

fun NavGraphBuilder.playerScreen() {
    composable<PlayerRoute> {

    }
}