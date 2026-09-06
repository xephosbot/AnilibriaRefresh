package com.xbot.sharedapp.ios

import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.navigation.TopLevelNavKey
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.search.navigation.SearchRoute

/** Shown when a destination has no SF Symbol of its own. */
internal const val FallbackSfSymbolName = "circle"

/** Mapped here, not on [TopLevelNavKey], so the shared route contract stays free of iOS specifics. */
internal val TopLevelNavKey.sfSymbolName: String?
    get() = when (this) {
        HomeRoute -> "house"
        SearchRoute -> "magnifyingglass"
        FavoriteRoute -> "heart"
        PreferenceRoute -> "gearshape"
        else -> null
    }

internal val TopLevelNavKey.tabIdentifier: String
    get() = this::class.simpleName ?: toString()
