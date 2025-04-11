package com.xbot.shared.ui.feature.favorite.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.shared.ui.feature.favorite.FavoriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object FavoriteRoute

fun NavGraphBuilder.favoriteSection() {
    composable<FavoriteRoute> {
        FavoriteScreen()
    }
}