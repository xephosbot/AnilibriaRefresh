package com.xbot.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.MultiChoiceChipGroup
import com.xbot.designsystem.components.RangeSlider
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.SingleChoiceChipGroup
import com.xbot.designsystem.components.TopSearchInputField
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.effects.ProvideShimmer
import com.xbot.designsystem.effects.shimmerUpdater
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.search.utils.stringRes
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

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

@OptIn(ExperimentalMaterial3Api::class)
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
    val sortingTypeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val filtersBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSortingType by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

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
                    placeholder = { Text(stringResource(R.string.search_bar_placeholder)) },
                    leadingIcon = {
                        IconButton(
                            onClick = { onBackClick() }
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { searchFieldState.clearText() }
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                    },
                )
                ChipGroup {
                    AssistChip(
                        onClick = {
                            showSortingType = true
                        },
                        label = { Text(text = stringResource(state.selectedSortingType.stringRes)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    AssistChip(
                        onClick = {
                            showFilters = true
                        },
                        label = { Text(text = "Filters") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        SearchResultScreen(
            items = searchResult,
            contentPadding = innerPadding,
            onRetry = { error, action ->
                onAction(SearchScreenAction.ShowErrorMessage(error, action))
            },
            onReleaseClick = onReleaseClick
        )
    }

    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = filtersBottomSheetState
        ) {
            FiltersScreen(
                state = state,
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
                    onAction(SearchScreenAction.UpdateYearsRange(yearsRange.toIntRange()))
                },
                onAgeRatingClick = { ageRating ->
                    onAction(SearchScreenAction.ToggleAgeRating(ageRating))
                }
            )
        }
    }

    if (showSortingType) {
        ModalBottomSheet(
            onDismissRequest = { showSortingType = false },
            sheetState = sortingTypeBottomSheetState
        ) {
            SortingTypeScreen(
                state = state,
                onSortingTypeClick = { sortingType ->
                    onAction(SearchScreenAction.UpdateSortingType(sortingType))
                },
            )
        }
    }
}

@Composable
private fun SearchResultScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    contentPadding: PaddingValues,
    onRetry: (Throwable, () -> Unit) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val showErrorMessage: (Throwable) -> Unit = { error ->
        onRetry(error) { items.retry() }
    }

    LaunchedEffect(items) {
        snapshotFlow { items.loadState }.collect { loadState ->
            (loadState.refresh as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.append as? LoadState.Error)?.let { showErrorMessage(it.error) }
            (loadState.prepend as? LoadState.Error)?.let { showErrorMessage(it.error) }
        }
    }

    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        AnimatedVisibility(
            visible = items.loadState.refresh !is LoadState.Loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchResultContent(
                modifier = modifier.shimmerUpdater(shimmer),
                items = items,
                contentPadding = contentPadding,
                onReleaseClick = onReleaseClick,
            )
        }
    }
}

@Composable
private fun SearchResultContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Release>,
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit,
) {
    val feedState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (items.itemCount > 0) feedState.scrollToItem(0)
    }

    Feed(
        modifier = modifier,
        columns = GridCells.Adaptive(350.dp),
        contentPadding = contentPadding,
        state = feedState
    ) {
        header(
            title = { Text(text = stringResource(R.string.label_search_results)) },
        )
        pagingItems(items) { index, release ->
            Column {
                ReleaseListItem(
                    modifier = Modifier.feedItemSpacing(index),
                    release = release,
                    onClick = { onReleaseClick(it.id) },
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SortingTypeScreen(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    onSortingTypeClick: (SortingType) -> Unit,
) {
    Crossfade(
        targetState = state.loading
    ) { targetState ->
        when (targetState) {
            true -> LoadingFiltersScreen(modifier)
            false -> {
                SortingTypeScreenContent(
                    modifier = modifier,
                    sortingTypes = state.sortingTypes,
                    selectedSortingType = state.selectedSortingType,
                    onSortingTypeClick = onSortingTypeClick
                )
            }
        }
    }
}

@Composable
private fun SortingTypeScreenContent(
    modifier: Modifier = Modifier,
    sortingTypes: List<SortingType>,
    selectedSortingType: SortingType,
    onSortingTypeClick: (SortingType) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Header(
            title = { Text(stringResource(R.string.label_sorting_types)) }
        )
        SingleChoiceChipGroup(
            items = sortingTypes,
            selectedItem = selectedSortingType
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onSortingTypeClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }
    }
}

@Composable
private fun FiltersScreen(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    onGenreClick: (Genre) -> Unit,
    onReleaseTypeClick: (ReleaseType) -> Unit,
    onPublishStatusClick: (PublishStatus) -> Unit,
    onProductionStatusClick: (ProductionStatus) -> Unit,
    onSeasonClick: (Season) -> Unit,
    onYearsRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onAgeRatingClick: (AgeRating) -> Unit,
) {
    Crossfade(
        targetState = state.loading
    ) { targetState ->
        when (targetState) {
            true -> LoadingFiltersScreen(modifier)
            false -> FiltersScreenContent(
                modifier = modifier,
                genres = state.genres,
                selectedGenres = state.selectedGenres.toList(),
                onGenreClick = onGenreClick,
                releaseTypes = state.releaseTypes,
                selectedReleaseTypes = state.selectedReleaseTypes.toList(),
                onReleaseTypeClick = onReleaseTypeClick,
                publishStatuses = state.publishStatuses,
                selectedPublishStatuses = state.selectedPublishStatuses.toList(),
                onPublishStatusClick = onPublishStatusClick,
                productionStatuses = state.productionStatuses,
                selectedProductionStatuses = state.selectedProductionStatuses.toList(),
                onProductionStatusClick = onProductionStatusClick,
                seasons = state.seasons,
                selectedSeasons = state.selectedSeasons.toList(),
                onSeasonClick = onSeasonClick,
                years = state.years.toFloatRange(),
                selectedYears = state.selectedYears.toFloatRange(),
                onYearsRangeChange = onYearsRangeChange,
                ageRatings = state.ageRatings,
                selectedAgeRatings = state.selectedAgeRatings.toList(),
                onAgeRatingClick = onAgeRatingClick
            )
        }
    }
}

@Composable
private fun FiltersScreenContent(
    modifier: Modifier = Modifier,
    genres: List<Genre>,
    selectedGenres: List<Genre>,
    onGenreClick: (Genre) -> Unit,
    releaseTypes: List<ReleaseType>,
    selectedReleaseTypes: List<ReleaseType>,
    onReleaseTypeClick: (ReleaseType) -> Unit,
    publishStatuses: List<PublishStatus>,
    selectedPublishStatuses: List<PublishStatus>,
    onPublishStatusClick: (PublishStatus) -> Unit,
    productionStatuses: List<ProductionStatus>,
    selectedProductionStatuses: List<ProductionStatus>,
    onProductionStatusClick: (ProductionStatus) -> Unit,
    seasons: List<Season>,
    selectedSeasons: List<Season>,
    onSeasonClick: (Season) -> Unit,
    years: ClosedFloatingPointRange<Float>,
    selectedYears: ClosedFloatingPointRange<Float>,
    onYearsRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    ageRatings: List<AgeRating>,
    selectedAgeRatings: List<AgeRating>,
    onAgeRatingClick: (AgeRating) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Header(
            title = { Text(stringResource(R.string.label_genres)) }
        )
        MultiChoiceChipGroup(
            items = genres,
            selectedItems = selectedGenres
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onGenreClick(item) },
                label = { Text(item.name) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }

        Header(
            title = { Text(stringResource(R.string.label_release_types)) }
        )
        MultiChoiceChipGroup(
            items = releaseTypes,
            selectedItems = selectedReleaseTypes
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onReleaseTypeClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }

        Header(
            title = { Text(stringResource(R.string.label_publish_statuses)) }
        )
        MultiChoiceChipGroup(
            items = publishStatuses,
            selectedItems = selectedPublishStatuses
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onPublishStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }

        Header(
            title = { Text(stringResource(R.string.label_production_statuses)) }
        )
        MultiChoiceChipGroup(
            items = productionStatuses,
            selectedItems = selectedProductionStatuses
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onProductionStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }

        Header(
            title = { Text(stringResource(R.string.label_seasons)) }
        )
        MultiChoiceChipGroup(
            items = seasons,
            selectedItems = selectedSeasons
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onSeasonClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }

        Header(
            title = { Text(stringResource(R.string.label_years)) }
        )
        RangeSlider(
            sliderPosition = selectedYears,
            onValueChange = onYearsRangeChange,
            valueRange = years,
        )

        Header(
            title = { Text(stringResource(R.string.label_age_ratings)) }
        )
        MultiChoiceChipGroup(
            items = ageRatings,
            selectedItems = selectedAgeRatings
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onAgeRatingClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = {
                    if (selected) Icon(AnilibriaIcons.Outlined.Check, null)
                }
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LoadingFiltersScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun ClosedRange<Int>.toFloatRange(): ClosedFloatingPointRange<Float> {
    return start.toFloat()..endInclusive.toFloat()
}

private fun ClosedFloatingPointRange<Float>.toIntRange(): ClosedRange<Int> {
    return start.roundToInt()..endInclusive.roundToInt()
}