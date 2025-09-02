package com.xbot.home.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.common.navigation.TopLevelDestination
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.home.HomeScreen
import com.xbot.resources.Res
import com.xbot.resources.tab_home
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data object HomeRoute : TopLevelDestination {
    override val textRes: StringResource
        get() = Res.string.tab_home
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Filled.Home
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Outlined.Home
}

fun NavGraphBuilder.homeSection(
    onSearchClick: NavBackStackEntry.() -> Unit,
    onReleaseClick: NavBackStackEntry.(Int) -> Unit
) {
    composable<HomeRoute> { navBackStackEntry ->
        HomeScreen(
            onSearchClick = {
                navBackStackEntry.onSearchClick()
            },
            onReleaseClick = { releaseId ->
                navBackStackEntry.onReleaseClick(releaseId)
            }
        )
    }
}
