package com.xbot.sharedapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.common.navigation.Navigator
import com.xbot.home.navigation.HomeRoute
import com.xbot.sharedapp.AnilibriaNavigator
import com.xbot.sharedapp.di.koinInjectAll
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
internal fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navigator: Navigator<NavBackStackEntry>,
    startNavKey: NavKey = HomeRoute,
    navEntryBuilders: List<NavEntryBuilder> = koinInjectAll()
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = (navigator as AnilibriaNavigator).navController,
        startDestination = startNavKey,
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        navEntryBuilders.forEach { builder ->
            builder(navigator)
        }
    }
}
