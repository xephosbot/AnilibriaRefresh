package com.xbot.sharedapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.DockedEdge
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberDragToResizeState
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberSupportingPaneSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.rememberSharedViewModelStoreNavEntryDecorator
import com.xbot.designsystem.utils.LocalIsSinglePane
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    KoinExperimentalAPI::class
)
@Composable
internal fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navigator: Navigator,
) {
    val dragToResizeState = rememberDragToResizeState(dockedEdge = DockedEdge.Bottom)
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
        .copy(
            horizontalPartitionSpacerSize = 0.dp,
            verticalPartitionSpacerSize = 0.dp
        )
    val supportingPaneSceneStrategy = rememberSupportingPaneSceneStrategy<NavKey>(
        directive = scaffoldDirective,
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        ),
        //TODO: Uncomment when Google finishes the Levitate strategy
        /*adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Levitate(
                alignment = Alignment.BottomCenter,
                dragToResizeState = dragToResizeState,
            ).onlyIfSinglePane(scaffoldDirective)
        ),*/
    )
    val listDetailSceneStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = scaffoldDirective,
    )
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

    CompositionLocalProvider(
        LocalIsSinglePane provides (scaffoldDirective.maxHorizontalPartitions == 1)
    ) {
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
                rememberSharedViewModelStoreNavEntryDecorator(),
            ),
            sceneStrategy = supportingPaneSceneStrategy then listDetailSceneStrategy then dialogStrategy,
            entryProvider = koinEntryProvider()
        )
    }
}
