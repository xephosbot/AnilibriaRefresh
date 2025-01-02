package com.xbot.anilibriarefresh.ui.feature.home.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Label
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.stringRes
import com.xbot.designsystem.components.Header
import com.xbot.domain.models.CatalogFilters
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun FiltersScreen(
    modifier: Modifier = Modifier,
    viewModel: FiltersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Crossfade(
        targetState = state
    ) { targetState ->
        when (targetState) {
            is FiltersScreenState.Loading -> LoadingFiltersScreen(modifier)
            is FiltersScreenState.Success -> FiltersScreenContent(modifier, targetState.filters)
        }
    }
}

@Composable
private fun FiltersScreenContent(
    modifier: Modifier = Modifier,
    filters: CatalogFilters
) {
    LazyColumn(modifier = modifier) {
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_genres)
            )
        }
        chipGroup(filters.genres) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = it.name) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_release_types)
            )
        }
        chipGroup(filters.releaseTypes) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_publish_statuses)
            )
        }
        chipGroup(filters.publishStatuses) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_production_statuses)
            )
        }
        chipGroup(filters.productionStatuses) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_sorting_types)
            )
        }
        chipGroup(filters.sortingTypes) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_seasons)
            )
        }
        chipGroup(filters.seasons) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_years)
            )
        }
        item {
            val minYear = filters.years.first().toFloat()
            val maxYear = filters.years.last().toFloat()
            var yearRange by remember { mutableStateOf(minYear..maxYear) }
            YearSlider(
                minValue = minYear,
                maxValue = maxYear,
                sliderPosition = yearRange,
                onValueChange = { newRange -> yearRange = newRange },
            )
        }
        item(
            contentType = SearchScreenContentType.Header
        ) {
            Header(
                title = stringResource(R.string.label_age_ratings)
            )
        }
        chipGroup(filters.ageRatings) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = stringResource(it.stringRes)) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
private fun <T> LazyListScope.chipGroup(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
) {
    item(
        contentType = SearchScreenContentType.ChipGroup
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                itemContent(item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearSlider(
    modifier: Modifier = Modifier,
    minValue: Float,
    maxValue: Float,
    steps: Int? = null,
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val calculatedSteps = steps ?: ((maxValue - minValue).toInt() - 1).coerceAtLeast(0)

    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = minValue.roundToInt().toString(),
            modifier = Modifier.semantics { contentDescription = "Minimum Year" },
        )

        Spacer(Modifier.width(8.dp))

        RangeSlider(
            modifier = Modifier.weight(1f),
            value = sliderPosition,
            steps = calculatedSteps,
            onValueChange = onValueChange,
            valueRange = minValue..maxValue,
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            startThumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier
                                .sizeIn(45.dp, 25.dp)
                                .wrapContentWidth()
                        ) {
                            Text("%.0f".format(sliderPosition.start))
                        }
                    },
                    interactionSource = startInteractionSource,
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = startInteractionSource,
                    )
                }
            },
            endThumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier
                                .sizeIn(45.dp, 25.dp)
                                .wrapContentWidth(),
                        ) {
                            Text("%.0f".format(sliderPosition.endInclusive))
                        }
                    },
                    interactionSource = endInteractionSource,
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = endInteractionSource,
                    )
                }
            },
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = maxValue.roundToInt().toString(),
            modifier = Modifier.semantics { contentDescription = "Maximum Year" },
        )
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

private enum class SearchScreenContentType {
    Header,
    ChipGroup
}