package com.xbot.sharedapp.ios

import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.navigation.TopLevelNavKey
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.search.navigation.SearchRoute

/**
 * SF Symbol backing each top level destination in the native tab bar.
 *
 * The mapping lives here rather than on [TopLevelNavKey] so the shared route contract stays free of
 * iOS specifics — the Compose chrome keeps using the `ImageVector` icons declared on the route.
 */
internal val TopLevelNavKey.sfSymbolName: String
    get() = when (this) {
        HomeRoute -> "house"
        SearchRoute -> "magnifyingglass"
        FavoriteRoute -> "heart"
        PreferenceRoute -> "gearshape"
        else -> "circle"
    }

/** Stable identifier used to match a `UITab` back to its route. */
internal val TopLevelNavKey.tabIdentifier: String
    get() = this::class.simpleName ?: toString()
