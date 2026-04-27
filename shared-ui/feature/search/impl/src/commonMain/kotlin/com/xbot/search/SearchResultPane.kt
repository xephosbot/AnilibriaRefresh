package com.xbot.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.TopSearchInputField
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Close
import com.xbot.designsystem.icons.Filter
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.xbot.designsystem.utils.AnilibriaPreviewWrapper
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.union
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.models.Release
import com.xbot.localization.StringResource
import com.xbot.localization.localizedMessage
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.button_filters
import com.xbot.resources.button_retry
import com.xbot.resources.label_search_results
import com.xbot.resources.search_bar_placeholder
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun SearchResultPane(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val state by viewModel.collectAsState()
    val searchResult = viewModel.searchResult.collectAsLazyPagingItems()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchScreenSideEffect.ShowErrorMessage -> {
                SnackbarManager.showMessage(
                    title = sideEffect.error.localizedMessage(),
                    action = MessageAction(
                        title = StringResource.Text(Res.string.button_retry),
                        action = sideEffect.onRetry,
                    )
                )
            }
        }
    }

    SearchResultPaneContent(
        modifier = modifier,
        state = state,
        searchResult = searchResult,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onFiltersClick = onFiltersClick,
        onReleaseClick = onReleaseClick
    )
}

@Composable
private fun SearchResultPaneContent(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    searchResult: LazyPagingItems<Release>,
    onAction: (SearchScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val searchFieldState = rememberTextFieldState(state.query)

    LaunchedEffect(searchFieldState) {
        snapshotFlow { searchFieldState.text.toString() }.collect { text ->
            onAction(SearchScreenAction.QueryChanged(text))
        }
    }

    val showErrorMessage: (Throwable) -> Unit = { _ ->
        searchResult.retry()
    }

    LaunchedEffect(searchResult) {
        snapshotFlow { searchResult.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { showErrorMessage(it.error) }
        }
    }

    val feedState = rememberLazyGridState()

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                TopSearchInputField(
                    modifier = Modifier.fillMaxWidth(),
                    state = searchFieldState,
                    onSearch = {},
                    placeholder = { Text(stringResource(Res.string.search_bar_placeholder)) },
                    leadingIcon = {
                        IconButton(
                            onClick = { onBackClick() }
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onFiltersClick() }
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Filter,
                                    contentDescription = stringResource(Res.string.button_filters)
                                )
                            }
                            IconButton(
                                onClick = { searchFieldState.clearText() }
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                )

                if (state.filters.hasActiveFilters) {
                    ChipGroup {
                        state.filters.selectedGenres.forEach { genre ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.ToggleGenre(genre)) },
                                label = { Text(genre.name) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        state.filters.selectedReleaseTypes.forEach { type ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.ToggleReleaseType(type)) },
                                label = { Text(stringResource(type.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        state.filters.selectedPublishStatuses.forEach { status ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.TogglePublishStatus(status)) },
                                label = { Text(stringResource(status.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        state.filters.selectedProductionStatuses.forEach { status ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.ToggleProductionStatus(status)) },
                                label = { Text(stringResource(status.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        state.filters.selectedSeasons.forEach { season ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.ToggleSeason(season)) },
                                label = { Text(stringResource(season.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        state.filters.selectedAgeRatings.forEach { rating ->
                            AssistChip(
                                onClick = { onAction(SearchScreenAction.ToggleAgeRating(rating)) },
                                label = { Text(stringResource(rating.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                HorizontalDivider()
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        SearchResultContent(
            modifier = modifier,
            items = searchResult,
            state = feedState,
            contentPadding = innerPadding,
            onReleaseClick = onReleaseClick,
        )
    }
}

@Composable
private fun SearchResultContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    state: LazyGridState,
    contentPadding: PaddingValues,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val columnsCount = remember {
        derivedStateOf { state.layoutInfo.maxSpan }
    }

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(400.dp),
            contentPadding = contentPadding.union(WindowInsets.ime.asPaddingValues()),
            state = state,
        ) {
            header(
                title = { Text(text = stringResource(Res.string.label_search_results)) },
            )
            
            pagingItems(
                items = items,
                loadingPlaceholderCount = 10,
            ) { index, release ->
                ReleaseListItem(
                    modifier = Modifier
                        .section(index, items.itemCount.takeIf { it > 0 } ?: 10, columnsCount.value),
                    release = release,
                    onClick = onReleaseClick,
                )
            }
        }
    }
}

@Preview
@PreviewWrapper(AnilibriaPreviewWrapper::class)
@Composable
private fun SearchResultPanePreview() {
    val searchResult = flowOf(PagingData.from(ReleaseFixtures.all)).collectAsLazyPagingItems()

    SearchResultPaneContent(
        searchResult = searchResult,
        state = SearchScreenState(),
        onAction = {},
        onBackClick = {},
        onFiltersClick = {},
        onReleaseClick = {}
    )
}
