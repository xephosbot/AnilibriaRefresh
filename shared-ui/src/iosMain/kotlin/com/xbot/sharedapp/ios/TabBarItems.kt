package com.xbot.sharedapp.ios

import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.navigation.TopLevelNavKey
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.search.navigation.SearchRoute

/** Shown when a destination has no SF Symbol of its own. */
internal const val FallbackSfSymbolName = "circle"

/**
 * SF Symbol backing each top level destination in the native tab bar, or `null` when unmapped.
 *
 * The mapping lives here rather than on [TopLevelNavKey] so the shared route contract stays free of
 * iOS specifics — the Compose bar keeps using the `ImageVector` icons declared on the route. The
 * trade-off is that `TopLevelNavKey` is an open interface, so a new destination cannot be caught by
 * the compiler; returning `null` instead of silently falling back lets the caller say so out loud.
 */
internal val TopLevelNavKey.sfSymbolName: String?
    get() = when (this) {
        HomeRoute -> "house"
        SearchRoute -> "magnifyingglass"
        FavoriteRoute -> "heart"
        PreferenceRoute -> "gearshape"
        else -> null
    }

/** Stable identifier used to match a `UITab` back to its route. */
internal val TopLevelNavKey.tabIdentifier: String
    get() = this::class.simpleName ?: toString()
