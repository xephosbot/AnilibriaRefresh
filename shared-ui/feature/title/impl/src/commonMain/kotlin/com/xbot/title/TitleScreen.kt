package com.xbot.title

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.NavigableSupportingPaneScaffold
import com.xbot.designsystem.components.ThreePaneScaffoldPredictiveBackHandler
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.components.isHidden
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onPlayClick = onPlayClick,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onAction: (TitleScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator<Unit>(
        scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
            .copy(horizontalPartitionSpacerSize = 0.dp),
        adaptStrategies = SupportingPaneScaffoldDefaults.adaptStrategies(
            supportingPaneAdaptStrategy = AdaptStrategy.Hide
        )
    )
    val scope = rememberCoroutineScope()
    val backBehavior =
        if (scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List) && scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.Detail)) {
            BackNavigationBehavior.PopUntilContentChange
        } else {
            BackNavigationBehavior.PopUntilScaffoldValueChange
        }

    NavigableSupportingPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        defaultBackBehavior = backBehavior,
        mainPane = {
            TitleDetailsPane(
                state = state,
                onBackClick = onBackClick,
                onPlayClick = onPlayClick,
                onReleaseClick = onReleaseClick,
                onEpisodesListClick = {
                    scope.launch {
                        scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                    }
                }
            )
        },
        supportingPane = {
            TitleEpisodesPane(
                state = state,
                showBackButton = scaffoldNavigator.isHidden(SupportingPaneScaffoldRole.Main),
                onBackClick = {
                    scope.launch {
                        if (scaffoldNavigator.canNavigateBack(backBehavior)) {
                            scaffoldNavigator.navigateBack(backBehavior)
                        }
                    }
                },
                onPlayClick = onPlayClick,
            )
        }
    )
}

