package com.xbot.search.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute : NavKey

fun Navigator<*>.navigateToSearch() {
    navigate(SearchRoute)
}