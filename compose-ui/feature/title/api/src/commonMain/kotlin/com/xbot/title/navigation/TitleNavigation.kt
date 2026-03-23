package com.xbot.title.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.domain.models.Release
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TitleRoute(
    val aliasOrId: String,
    @Transient
    val release: Release? = null
) : NavKey

fun Navigator.navigateToTitle(id: Int) {
    navigate(TitleRoute(id.toString()))
}

fun Navigator.navigateToTitle(release: Release) {
    navigate(TitleRoute(release.id.toString(), release))
}
