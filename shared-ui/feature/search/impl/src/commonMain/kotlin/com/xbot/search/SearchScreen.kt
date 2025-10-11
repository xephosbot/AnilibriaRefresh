package com.xbot.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.AnimatedPane
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.xbot.designsystem.components.NavigableSupportingPaneScaffold
import com.xbot.designsystem.components.isExpanded
import com.xbot.designsystem.components.isHidden
import com.xbot.domain.models.Release
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    onBackClick: () -> Unit,
    onReleaseClick: (Int) -> Unit
) {
    val searchResult = viewModel.searchResult.collectAsLazyPagingItems()
    val state by viewModel.filters.collectAsStateWithLifecycle()

    SearchScreenContent(
        modifier = modifier,
        searchResult = searchResult,
        state = state,
        searchFieldState = viewModel.searchFieldState,
        onBackClick = onBackClick,
        onReleaseClick = onReleaseClick,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    searchResult: LazyPagingItems<Release>,
    state: SearchScreenState,
    searchFieldState: TextFieldState,
    onAction: (SearchScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator<Unit>(
        scaffoldDirective = calculatePaneScaffoldDirective(
            currentWindowAdaptiveInfo()
        ).copy(
            horizontalPartitionSpacerSize = 0.dp,
            verticalPartitionSpacerSize = 0.dp
        ),
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
            AnimatedPane {
                SearchResultPane(
                    items = searchResult,
                    searchFieldState = searchFieldState,
                    onRetry = { error, action ->
                        onAction(SearchScreenAction.ShowErrorMessage(error, action))
                    },
                    onBackClick = onBackClick,
                    onShowFilters = {
                        scope.launch {
                            scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    },
                    onReleaseClick = onReleaseClick
                )
            }
        },
        supportingPane = scaffoldNavigator.currentDestination?.let {
            {
                AnimatedPane {
                    SearchFilterPane(
                        showBackButton = true,
                        onBackClick = {
                            scope.launch {
                                scaffoldNavigator.navigateBack(backBehavior)
                            }
                        },
                        state = state,
                        onSortingTypeClick = { sortingType ->
                            onAction(SearchScreenAction.UpdateSortingType(sortingType))
                        },
                        onGenreClick = { genre ->
                            onAction(SearchScreenAction.ToggleGenre(genre))
                        },
                        onReleaseTypeClick = { releaseType ->
                            onAction(SearchScreenAction.ToggleReleaseType(releaseType))
                        },
                        onPublishStatusClick = { publishStatus ->
                            onAction(SearchScreenAction.TogglePublishStatus(publishStatus))
                        },
                        onProductionStatusClick = { productionStatus ->
                            onAction(SearchScreenAction.ToggleProductionStatus(productionStatus))
                        },
                        onSeasonClick = { season ->
                            onAction(SearchScreenAction.ToggleSeason(season))
                        },
                        onYearsRangeChange = { yearsRange ->
                            onAction(SearchScreenAction.UpdateYearsRange(yearsRange))
                        },
                        onAgeRatingClick = { ageRating ->
                            onAction(SearchScreenAction.ToggleAgeRating(ageRating))
                        }
                    )
                }
            }
        } ?: {}
    )
}