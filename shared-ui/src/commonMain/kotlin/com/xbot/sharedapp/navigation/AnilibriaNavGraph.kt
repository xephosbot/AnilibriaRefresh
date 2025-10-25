package com.xbot.sharedapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberSupportingPaneSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.common.navigation.NavKey
import com.xbot.sharedapp.AnilibriaNavigator
import com.xbot.sharedapp.di.koinInjectAll
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navigator: AnilibriaNavigator,
    navEntryBuilders: List<NavEntryBuilder> = koinInjectAll()
) {
    val supportingPaneSceneStrategy = rememberSupportingPaneSceneStrategy<NavKey>(
        directive = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
            .copy(
                horizontalPartitionSpacerSize = 0.dp,
                verticalPartitionSpacerSize = 0.dp
            ),
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        ),
    )
    val listDetailSceneStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
            .copy(
                horizontalPartitionSpacerSize = 0.dp,
                verticalPartitionSpacerSize = 0.dp
            ),
    )

    NavDisplay(
        backStack = navigator.backStack,
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
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        sceneStrategy = supportingPaneSceneStrategy then listDetailSceneStrategy,
        entryProvider = entryProvider {
            navEntryBuilders.forEach { builder ->
                builder(navigator)
            }
        }
    )
}
