package com.xbot.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.activity
import com.xbot.player.PlayerActivity
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(val id: Int)

fun NavHostController.navigateToPlayer(id: Int) = navigate(PlayerRoute(id))

fun NavGraphBuilder.playerScreen() {
    activity<PlayerRoute> {
        activityClass = PlayerActivity::class
    }
}