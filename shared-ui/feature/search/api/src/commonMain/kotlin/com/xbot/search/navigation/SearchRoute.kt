package com.xbot.search.navigation

import com.xbot.common.navigation.Destination
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute : Destination

fun Navigator<*>.navigateToSearch() {
    navigate(SearchRoute)
}