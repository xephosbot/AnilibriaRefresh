package com.xbot.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.Destination
import com.xbot.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute : Destination {
    override val isTopLevel: Boolean = true
}

fun NavGraphBuilder.profileSection(
    onReleaseClick: (Int) -> Unit,
) {
    composable<ProfileRoute> {
        ProfileScreen(
            onReleaseClick = onReleaseClick,
        )
    }
}