package com.xbot.shared.ui.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.shared.ui.feature.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute

fun NavGraphBuilder.profileSection() {
    composable<ProfileRoute> {
        ProfileScreen()
    }
}