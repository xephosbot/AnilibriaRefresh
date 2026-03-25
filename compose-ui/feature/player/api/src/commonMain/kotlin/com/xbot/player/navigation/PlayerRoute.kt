package com.xbot.player.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    val aliasOrId: String,
    val episodeOrdinal: Int,
) : NavKey

fun Navigator.navigateToPlayer(releaseId: Int, episodeOrdinal: Int) {
    navigate(PlayerRoute(releaseId.toString(), episodeOrdinal))
}
