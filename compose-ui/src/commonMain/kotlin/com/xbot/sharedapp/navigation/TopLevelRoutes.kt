package com.xbot.sharedapp.navigation

import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.preference.navigation.PreferenceRoute

val TopLevelRoutes: Set<TopLevelNavKey> = setOf(HomeRoute, FavoriteRoute, PreferenceRoute)
