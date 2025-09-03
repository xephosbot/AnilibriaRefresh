package com.xbot.player.navigation

import com.xbot.common.navigation.Destination
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    val releaseId: Int,
    val episodeOrdinal: Int,
) : Destination

fun Navigator<*>.navigateToPlayer(releaseId: Int, episodeOrdinal: Int) {
    navigate(PlayerRoute(releaseId, episodeOrdinal))
}