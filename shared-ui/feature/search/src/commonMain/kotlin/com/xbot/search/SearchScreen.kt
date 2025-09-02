package com.xbot.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.resources.Res
import com.xbot.resources.button_filters
import com.xbot.resources.label_age_ratings
import com.xbot.resources.label_genres
import com.xbot.resources.label_production_statuses
import com.xbot.resources.label_publish_statuses
import com.xbot.resources.label_release_types
import com.xbot.resources.label_search_results
import com.xbot.resources.label_seasons
import com.xbot.resources.label_sorting_types
import com.xbot.resources.label_years
import com.xbot.resources.search_bar_placeholder
import com.xbot.designsystem.components.AssistChip
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.ModalScrollableBottomSheet
import com.xbot.designsystem.components.MultiChoiceChipGroup
import com.xbot.designsystem.components.RangeSlider
import com.xbot.designsystem.components.ReleaseListItem
import com.xbot.designsystem.components.TopSearchInputField
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.pagingItems
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.animatePlacement
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.union
import com.xbot.localization.stringRes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
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
                    placeholder = { Text(stringResource(Res.string.search_bar_placeholder)) },
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
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                imageVector = AnilibriaIcons.Outlined.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    AssistChip(
                        onClick = {
                            showFilters = true
                        },
                        label = { Text(text = stringResource(Res.string.button_filters)) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                imageVector = AnilibriaIcons.Outlined.ArrowDropDown,
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

    val sortingTypeScrollState = rememberScrollState()
    val filtersScrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    if (showFilters) {
        ModalScrollableBottomSheet(
            onDismissRequest = {
                scope.launch {
                    filtersScrollState.scrollTo(0)
                }
                showFilters = false
            },
            scrollableState = filtersScrollState
        ) {
            FiltersScreen(
                state = state,
                scrollState = filtersScrollState,
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
        ModalScrollableBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sortingTypeScrollState.scrollTo(0)
                }
                showSortingType = false
            },
            scrollableState = sortingTypeScrollState
        ) {
            SortingTypeScreen(
                state = state,
                scrollState = sortingTypeScrollState,
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

    val feedState = rememberLazyGridState()
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        SearchResultContent(
            modifier = modifier.shimmerUpdater(shimmer),
            items = items,
            state = feedState,
            contentPadding = contentPadding,
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
    onReleaseClick: (Int) -> Unit,
) {
    Feed(
        modifier = modifier,
        columns = GridCells.Adaptive(350.dp),
        contentPadding = contentPadding.union(WindowInsets.ime.asPaddingValues()),
        state = state,
    ) {
        header(
            title = { Text(text = stringResource(Res.string.label_search_results)) },
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
    state: SearchScreenState,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    onSortingTypeClick: (SortingType) -> Unit,
) {
    Crossfade(
        targetState = state.loading
    ) { targetState ->
        when (targetState) {
            true -> LoadingFiltersScreen(modifier)
            false -> {
                SortingTypeScreenContent(
                    modifier = modifier.verticalScroll(scrollState),
                    sortingTypes = state.sortingTypes,
                    selectedSortingType = state.selectedSortingType,
                    onSortingTypeClick = onSortingTypeClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SortingTypeScreenContent(
    modifier: Modifier = Modifier,
    sortingTypes: List<SortingType>,
    selectedSortingType: SortingType,
    onSortingTypeClick: (SortingType) -> Unit,
) {
    Column(modifier = modifier) {
        Header(
            title = { Text(stringResource(Res.string.label_sorting_types)) }
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sortingTypes.forEachIndexed { index, item ->
                OutlinedToggleButton(
                    modifier = Modifier.fillMaxWidth(),
                    checked = selectedSortingType == item,
                    onCheckedChange = { onSortingTypeClick(item) },
                ) {
                    if (selectedSortingType == item) {
                        Icon(AnilibriaIcons.Outlined.Check, null)
                        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    }
                    Text(stringResource(item.stringRes))
                }
            }
        }
    }
}

@Composable
private fun FiltersScreen(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    scrollState: ScrollState,
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
                modifier = modifier.verticalScroll(scrollState),
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
    Column(modifier = modifier) {
        Header(
            title = { Text(stringResource(Res.string.label_genres)) }
        )
        MultiChoiceChipGroup(
            items = genres,
            selectedItems = selectedGenres
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onGenreClick(item) },
                label = { Text(item.name) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_release_types)) }
        )
        MultiChoiceChipGroup(
            items = releaseTypes,
            selectedItems = selectedReleaseTypes
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onReleaseTypeClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_publish_statuses)) }
        )
        MultiChoiceChipGroup(
            items = publishStatuses,
            selectedItems = selectedPublishStatuses
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onPublishStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_production_statuses)) }
        )
        MultiChoiceChipGroup(
            items = productionStatuses,
            selectedItems = selectedProductionStatuses
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onProductionStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_seasons)) }
        )
        MultiChoiceChipGroup(
            items = seasons,
            selectedItems = selectedSeasons
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onSeasonClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_years)) }
        )
        RangeSlider(
            sliderPosition = selectedYears,
            onValueChange = onYearsRangeChange,
            valueRange = years,
        )

        Header(
            title = { Text(stringResource(Res.string.label_age_ratings)) }
        )
        MultiChoiceChipGroup(
            items = ageRatings,
            selectedItems = selectedAgeRatings
        ) { selected, item ->
            FilterChip(
                modifier = Modifier.animatePlacement(),
                selected = selected,
                onClick = { onAgeRatingClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Outlined.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LoadingFiltersScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
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