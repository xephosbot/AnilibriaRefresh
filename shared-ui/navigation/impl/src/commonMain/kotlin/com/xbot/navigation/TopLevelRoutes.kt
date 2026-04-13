package com.xbot.navigation

import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.search.navigation.SearchRoute

val TopLevelRoutes: Set<TopLevelNavKey> = setOf(HomeRoute, SearchRoute, FavoriteRoute, PreferenceRoute)
