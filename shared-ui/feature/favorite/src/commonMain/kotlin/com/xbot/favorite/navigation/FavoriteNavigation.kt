package com.xbot.favorite.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.favorite.FavoriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object FavoriteRoute : Destination {
    override val isTopLevel: Boolean = true
}

fun NavGraphBuilder.favoriteSection() {
    composable<FavoriteRoute> {
        FavoriteScreen()
    }
}