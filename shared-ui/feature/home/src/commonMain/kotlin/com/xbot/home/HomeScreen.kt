package com.xbot.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xbot.designsystem.components.NavigableSupportingPaneScaffold
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.components.isHidden
import com.xbot.domain.models.Release
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onSearchClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val items = viewModel.releases.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        state = state,
        items = items,
        onAction = viewModel::onAction,
        onSearchClick = onSearchClick,
        onReleaseClick = onReleaseClick,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    items: LazyPagingItems<Release>,
    onAction: (HomeScreenAction) -> Unit,
    onSearchClick: () -> Unit,
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
        if (scaffoldNavigator.isExpanded(SupportingPaneScaffoldRole.Main) && scaffoldNavigator.isExpanded(SupportingPaneScaffoldRole.Supporting)) {
            BackNavigationBehavior.PopUntilContentChange
        } else {
            BackNavigationBehavior.PopUntilScaffoldValueChange
        }

    NavigableSupportingPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        defaultBackBehavior = backBehavior,
        mainPane = {
            FeedPane(
                state = state,
                items = items,
                onSearchClick = onSearchClick,
                onScheduleClick = {
                    scope.launch {
                        scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                    }
                },
                onReleaseClick = onReleaseClick,
                onRefresh = {
                    items.refresh()
                    onAction(HomeScreenAction.Refresh)
                },
                onShowErrorMessage = { error ->
                    onAction(HomeScreenAction.ShowErrorMessage(error) { items.retry() })
                }
            )
        },
        supportingPane = {
            SchedulePane(
                state = state,
                showBackButton = scaffoldNavigator.isHidden(SupportingPaneScaffoldRole.Main),
                onReleaseClick = onReleaseClick,
                onBackClick = {
                    scope.launch {
                        if (scaffoldNavigator.canNavigateBack(backBehavior)) {
                            scaffoldNavigator.navigateBack(backBehavior)
                        }
                    }
                }
            )
        }
    )
}
