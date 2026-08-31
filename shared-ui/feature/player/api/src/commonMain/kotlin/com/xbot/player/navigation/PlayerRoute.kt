package com.xbot.player.navigation

import com.xbot.navigation.NavKey
import com.xbot.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    val releaseId: Int,
    val episodeOrdinal: Int,
) : NavKey {
    override val hidesNavigationBar: Boolean get() = true
}

fun Navigator.navigateToPlayer(releaseId: Int, episodeOrdinal: Int) {
    navigate(PlayerRoute(releaseId, episodeOrdinal))
}
