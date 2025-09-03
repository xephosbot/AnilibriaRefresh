package com.xbot.title.navigation

import com.xbot.common.navigation.Destination
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class TitleRoute(val aliasOrId: String) : Destination

fun Navigator<*>.navigateToTitle(id: Int) {
    navigate(TitleRoute(id.toString()))
}