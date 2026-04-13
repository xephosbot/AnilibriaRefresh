package com.xbot.search.navigation

import com.xbot.navigation.NavKey
import com.xbot.navigation.Navigator
import com.xbot.navigation.TopLevelNavKey
import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import com.xbot.resources.Res
import com.xbot.resources.fab_search
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Search

@Serializable
data object SearchRoute : TopLevelNavKey {
    override val textRes: StringResource
        get() = Res.string.fab_search
    override val selectedIcon: ImageVector
        get() = AnilibriaIcons.Search
    override val unselectedIcon: ImageVector
        get() = AnilibriaIcons.Search
}

@Serializable
data object SearchFiltersRoute : NavKey

fun Navigator.navigateToSearch() {
    navigate(SearchRoute)
}

fun Navigator.navigateToSearchFilters() {
    navigate(SearchFiltersRoute)
}
