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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
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
import com.xbot.designsystem.utils.union
import com.xbot.domain.models.Release
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.button_filters
import com.xbot.resources.label_search_results
import com.xbot.resources.search_bar_placeholder
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SearchResultPane(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onShowFilters: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val searchResult = viewModel.searchResult.collectAsLazyPagingItems()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()

    val showErrorMessage: (Throwable) -> Unit = { error ->
        viewModel.onAction(
            SearchScreenAction.ShowErrorMessage(
                error = error,
                onConfirmAction = { searchResult.retry() }
            )
        )
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
                    state = viewModel.searchFieldState,
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
                                onClick = { onShowFilters() }
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Filter,
                                    contentDescription = stringResource(Res.string.button_filters)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.searchFieldState.clearText() }
                            ) {
                                Icon(
                                    imageVector = AnilibriaIcons.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                )

                val hasFilters = selectedFilters.selectedGenres.isNotEmpty() ||
                        selectedFilters.selectedReleaseTypes.isNotEmpty() ||
                        selectedFilters.selectedPublishStatuses.isNotEmpty() ||
                        selectedFilters.selectedProductionStatuses.isNotEmpty() ||
                        selectedFilters.selectedSeasons.isNotEmpty() ||
                        selectedFilters.selectedAgeRatings.isNotEmpty()

                if (hasFilters) {
                    ChipGroup {
                        selectedFilters.selectedGenres.forEach { genre ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.ToggleGenre(genre)) },
                                label = { Text(genre.name) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        selectedFilters.selectedReleaseTypes.forEach { type ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.ToggleReleaseType(type)) },
                                label = { Text(stringResource(type.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        selectedFilters.selectedPublishStatuses.forEach { status ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.TogglePublishStatus(status)) },
                                label = { Text(stringResource(status.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        selectedFilters.selectedProductionStatuses.forEach { status ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.ToggleProductionStatus(status)) },
                                label = { Text(stringResource(status.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        selectedFilters.selectedSeasons.forEach { season ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.ToggleSeason(season)) },
                                label = { Text(stringResource(season.stringRes)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = AnilibriaIcons.Close,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        selectedFilters.selectedAgeRatings.forEach { rating ->
                            AssistChip(
                                onClick = { viewModel.onAction(SearchScreenAction.ToggleAgeRating(rating)) },
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
            onReleaseClick = { onReleaseClick(it.id) },
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
            pagingItems(items) { index, release ->
                ReleaseListItem(
                    modifier = Modifier
                        .section(index, items.itemCount, columnsCount.value),
                    release = release,
                    onClick = onReleaseClick,
                )
            }
        }
    }
}
