package com.xbot.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.components.MultiChoiceChipGroup
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.RangeSlider
import com.xbot.designsystem.components.SingleChoiceChipGroup
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Check
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.fixtures.data.genreMocks
import com.xbot.localization.stringRes
import com.xbot.resources.Res
import com.xbot.resources.description_filter_age_ratings
import com.xbot.resources.description_filter_genres
import com.xbot.resources.description_filter_production_statuses
import com.xbot.resources.description_filter_publish_statuses
import com.xbot.resources.description_filter_release_types
import com.xbot.resources.description_filter_seasons
import com.xbot.resources.description_filter_sorting_types
import com.xbot.resources.description_filter_years
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

@Composable
internal fun SearchFilterPane(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    showBackButton: Boolean,
    onNavigateBack: () -> Unit,
) {
    val availableFilters by viewModel.availableFilters.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()

    SearchFilterPaneContent(
        modifier = modifier,
        availableFilters = availableFilters,
        selectedFilters = selectedFilters,
        showBackButton = showBackButton,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SearchFilterPaneContent(
    modifier: Modifier = Modifier,
    availableFilters: CatalogFilters?,
    selectedFilters: SearchFiltersState,
    showBackButton: Boolean,
    onAction: (SearchScreenAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (showBackButton) {
                        FilledTonalIconButton(
                            modifier = Modifier.padding(start = 6.dp),
                            onClick = onNavigateBack,
                            shapes = IconButtonDefaults.shapes(),
                            colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colorScheme.surfaceContainerHighest)
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer),
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
                        onAction(SearchScreenAction.UpdateSortingType(it))
                    },
                    genres = targetState.genres,
                    selectedGenres = selectedFilters.selectedGenres.toList(),
                    onGenreClick = {
                        onAction(SearchScreenAction.ToggleGenre(it))
                    },
                    releaseTypes = targetState.types,
                    selectedReleaseTypes = selectedFilters.selectedReleaseTypes.toList(),
                    onReleaseTypeClick = {
                        onAction(SearchScreenAction.ToggleReleaseType(it))
                    },
                    publishStatuses = targetState.publishStatuses,
                    selectedPublishStatuses = selectedFilters.selectedPublishStatuses.toList(),
                    onPublishStatusClick = {
                        onAction(SearchScreenAction.TogglePublishStatus(it))
                    },
                    productionStatuses = targetState.productionStatuses,
                    selectedProductionStatuses = selectedFilters.selectedProductionStatuses.toList(),
                    onProductionStatusClick = {
                        onAction(SearchScreenAction.ToggleProductionStatus(it))
                    },
                    seasons = targetState.seasons,
                    selectedSeasons = selectedFilters.selectedSeasons.toList(),
                    onSeasonClick = {
                        onAction(SearchScreenAction.ToggleSeason(it))
                    },
                    years = targetState.years,
                    selectedYears = selectedFilters.selectedYears,
                    onYearsRangeChange = {
                        onAction(SearchScreenAction.UpdateYearsRange(it))
                    },
                    ageRatings = targetState.ageRatings,
                    selectedAgeRatings = selectedFilters.selectedAgeRatings.toList(),
                    onAgeRatingClick = {
                        onAction(SearchScreenAction.ToggleAgeRating(it))
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
    ) {
        PreferenceItem(
            modifier = Modifier.section(0, 8),
            headlineContent = { Text(stringResource(Res.string.label_sorting_types)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_sorting_types),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SingleChoiceChipGroup(
                        items = sortingTypes,
                        selectedItem = selectedSortingType,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(1, 8),
            headlineContent = { Text(stringResource(Res.string.label_genres)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_genres),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = genres,
                        selectedItems = selectedGenres,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(2, 8),
            headlineContent = { Text(stringResource(Res.string.label_release_types)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_release_types),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = releaseTypes,
                        selectedItems = selectedReleaseTypes,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(3, 8),
            headlineContent = { Text(stringResource(Res.string.label_publish_statuses)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_publish_statuses),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = publishStatuses,
                        selectedItems = selectedPublishStatuses,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(4, 8),
            headlineContent = { Text(stringResource(Res.string.label_production_statuses)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_production_statuses),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = productionStatuses,
                        selectedItems = selectedProductionStatuses,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(5, 8),
            headlineContent = { Text(stringResource(Res.string.label_seasons)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_seasons),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = seasons,
                        selectedItems = selectedSeasons,
                        contentPadding = PaddingValues()
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
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(6, 8),
            headlineContent = { Text(stringResource(Res.string.label_years)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_years),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    RangeSlider(
                        sliderPosition = selectedYears.toFloatRange(),
                        onValueChange = {
                            onYearsRangeChange(it.toIntRange())
                        },
                        valueRange = years.toFloatRange(),
                    )
                }
            }
        )

        PreferenceItem(
            modifier = Modifier.section(7, 8),
            headlineContent = { Text(stringResource(Res.string.label_age_ratings)) },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(Res.string.description_filter_age_ratings),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MultiChoiceChipGroup(
                        items = ageRatings,
                        selectedItems = selectedAgeRatings,
                        contentPadding = PaddingValues()
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
                }
            }
        )
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

@Preview
@Composable
private fun SearchFilterPanePreview(
    @PreviewParameter(SearchFiltersStateProvider::class) state: Pair<CatalogFilters?, SearchFiltersState>
) {
    AnilibriaPreview {
        SearchFilterPaneContent(
            availableFilters = state.first,
            selectedFilters = state.second,
            showBackButton = true,
            onAction = {},
            onNavigateBack = {},
        )
    }
}

private class SearchFiltersStateProvider : PreviewParameterProvider<Pair<CatalogFilters?, SearchFiltersState>> {
    override val values = sequenceOf(
        null to SearchFiltersState(),
        CatalogFilters(
            genres = genreMocks,
            types = ReleaseType.entries,
            publishStatuses = PublishStatus.entries,
            productionStatuses = ProductionStatus.entries,
            sortingTypes = SortingType.entries,
            seasons = Season.entries,
            ageRatings = AgeRating.entries,
            years = 1990..2024
        ) to SearchFiltersState(
            selectedYears = 2000..2020,
            selectedGenres = setOf(genreMocks.first())
        )
    )
}
