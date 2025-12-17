package com.xbot.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.MultiChoiceChipGroup
import com.xbot.designsystem.components.RangeSlider
import com.xbot.designsystem.components.SingleChoiceChipGroup
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Check
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.label_age_ratings
import com.xbot.resources.label_genres
import com.xbot.resources.label_production_statuses
import com.xbot.resources.label_publish_statuses
import com.xbot.resources.label_release_types
import com.xbot.resources.label_seasons
import com.xbot.resources.label_sorting_types
import com.xbot.resources.label_years
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchFilterPane(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    showBackButton: Boolean,
    onBackClick: () -> Unit,
) {
    val availableFilters by viewModel.availableFilters.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        Crossfade(
            targetState = availableFilters
        ) { targetState ->
            when (targetState) {
                null -> LoadingFiltersScreen(modifier)
                else -> FiltersScreenContent(
                    modifier = modifier,
                    contentPadding = innerPadding,
                    sortingTypes = targetState.sortingTypes,
                    selectedSortingType = selectedFilters.selectedSortingType,
                    onSortingTypeClick = {
                        viewModel.onAction(SearchScreenAction.UpdateSortingType(it))
                    },
                    genres = targetState.genres,
                    selectedGenres = selectedFilters.selectedGenres.toList(),
                    onGenreClick = {
                        viewModel.onAction(SearchScreenAction.ToggleGenre(it))
                    },
                    releaseTypes = targetState.types,
                    selectedReleaseTypes = selectedFilters.selectedReleaseTypes.toList(),
                    onReleaseTypeClick = {
                        viewModel.onAction(SearchScreenAction.ToggleReleaseType(it))
                    },
                    publishStatuses = targetState.publishStatuses,
                    selectedPublishStatuses = selectedFilters.selectedPublishStatuses.toList(),
                    onPublishStatusClick = {
                        viewModel.onAction(SearchScreenAction.TogglePublishStatus(it))
                    },
                    productionStatuses = targetState.productionStatuses,
                    selectedProductionStatuses = selectedFilters.selectedProductionStatuses.toList(),
                    onProductionStatusClick = {
                        viewModel.onAction(SearchScreenAction.ToggleProductionStatus(it))
                    },
                    seasons = targetState.seasons,
                    selectedSeasons = selectedFilters.selectedSeasons.toList(),
                    onSeasonClick = {
                        viewModel.onAction(SearchScreenAction.ToggleSeason(it))
                    },
                    years = targetState.years,
                    selectedYears = selectedFilters.selectedYears,
                    onYearsRangeChange = {
                        viewModel.onAction(SearchScreenAction.UpdateYearsRange(it))
                    },
                    ageRatings = targetState.ageRatings,
                    selectedAgeRatings = selectedFilters.selectedAgeRatings.toList(),
                    onAgeRatingClick = {
                        viewModel.onAction(SearchScreenAction.ToggleAgeRating(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun FiltersScreenContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    sortingTypes: List<SortingType>,
    selectedSortingType: SortingType,
    onSortingTypeClick: (SortingType) -> Unit,
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
    years: IntRange,
    selectedYears: IntRange,
    onYearsRangeChange: (IntRange) -> Unit,
    ageRatings: List<AgeRating>,
    selectedAgeRatings: List<AgeRating>,
    onAgeRatingClick: (AgeRating) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        Header(
            title = { Text(stringResource(Res.string.label_sorting_types)) }
        )
        SingleChoiceChipGroup(
            items = sortingTypes,
            selectedItem = selectedSortingType
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onSortingTypeClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_genres)) }
        )
        MultiChoiceChipGroup(
            items = genres,
            selectedItems = selectedGenres
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onGenreClick(item) },
                label = { Text(item.name) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
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
                selected = selected,
                onClick = { onReleaseTypeClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
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
                selected = selected,
                onClick = { onPublishStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
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
                selected = selected,
                onClick = { onProductionStatusClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
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
                selected = selected,
                onClick = { onSeasonClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
                    }
                } else null
            )
        }

        Header(
            title = { Text(stringResource(Res.string.label_years)) }
        )
        RangeSlider(
            sliderPosition = selectedYears.toFloatRange(),
            onValueChange = {
                onYearsRangeChange(it.toIntRange())
            },
            valueRange = years.toFloatRange(),
        )

        Header(
            title = { Text(stringResource(Res.string.label_age_ratings)) }
        )
        MultiChoiceChipGroup(
            items = ageRatings,
            selectedItems = selectedAgeRatings
        ) { selected, item ->
            FilterChip(
                selected = selected,
                onClick = { onAgeRatingClick(item) },
                label = { Text(stringResource(item.stringRes)) },
                leadingIcon = if (selected) {
                    {
                        Icon(AnilibriaIcons.Check, null, Modifier.size(FilterChipDefaults.IconSize))
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
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun IntRange.toFloatRange(): ClosedFloatingPointRange<Float> {
    return start.toFloat()..endInclusive.toFloat()
}

private fun ClosedFloatingPointRange<Float>.toIntRange(): IntRange {
    return start.roundToInt()..endInclusive.roundToInt()
}