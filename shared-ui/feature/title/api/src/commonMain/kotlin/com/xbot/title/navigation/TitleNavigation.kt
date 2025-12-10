package com.xbot.title.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class TitleRoute(val aliasOrId: String) : NavKey

@Serializable
data class TitleEpisodesRoute(val aliasOrId: String) : NavKey

fun Navigator.navigateToTitle(id: Int) {
    if (backStack.lastOrNull() is TitleEpisodesRoute) {
        navigateBack()
    }
    navigateBack()
    navigate(TitleRoute(id.toString()))
}
