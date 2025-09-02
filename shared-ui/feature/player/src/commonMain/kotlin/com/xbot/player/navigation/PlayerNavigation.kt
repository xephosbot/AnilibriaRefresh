package com.xbot.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.player.PlayerScreen
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    val releaseId: Int,
    val episodeOrdinal: Int,
) : Destination

fun NavGraphBuilder.playerScreen(
    onBackClick: () -> Unit,
) {
    composable<PlayerRoute> {
        PlayerScreen(
            onBackClick = onBackClick,
        )
    }
}