package com.xbot.sharedapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.sharedapp.AnilibriaNavigator
import com.xbot.sharedapp.di.koinInjectAll
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navigator: AnilibriaNavigator,
    navEntryBuilders: List<NavEntryBuilder> = koinInjectAll()
) {
    NavDisplay(
        backStack = navigator.backstack,
        modifier = modifier.fillMaxSize(),
        onBack = { navigator.navigateBack() },
        transitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        popTransitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        predictivePopTransitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            navEntryBuilders.forEach { builder ->
                builder(navigator)
            }
        }
    )
}
