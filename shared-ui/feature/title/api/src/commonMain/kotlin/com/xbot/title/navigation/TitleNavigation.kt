package com.xbot.title.navigation

import com.xbot.domain.models.Release
import com.xbot.navigation.ExternalUriNavKey
import com.xbot.navigation.NavKey
import com.xbot.navigation.Navigator
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TitleRoute(
    val aliasOrId: String,
    @Transient
    val release: Release? = null
) : NavKey

@Serializable
data class TitleLinkRoute(override val uri: String) : ExternalUriNavKey

fun Navigator.navigateToTitle(id: Int) {
    navigate(TitleRoute(id.toString()))
}

fun Navigator.navigateToTitle(release: Release) {
    navigate(TitleRoute(release.id.toString(), release))
}

fun Navigator.navigateToTitleLink(uri: String) {
    navigate(TitleLinkRoute(uri))
}
