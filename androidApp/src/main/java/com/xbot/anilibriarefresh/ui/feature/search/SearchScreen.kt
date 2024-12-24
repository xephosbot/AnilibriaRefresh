package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.ui.components.LocalNavigationPadding
import com.xbot.anilibriarefresh.ui.components.Scaffold
import com.xbot.anilibriarefresh.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.icons.Search
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.anilibriarefresh.ui.utils.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val state by searchViewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        modifier = modifier,
        state = state,
        onAction = searchViewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    onAction: (SearchScreenAction) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = modifier,
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Поиск по названию") },
                        leadingIcon = {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.Search,
                                contentDescription = null
                            )
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                FiltersScreen(state = state)
            }
        }
    ) {

    }
}

@Composable
private fun FiltersScreen(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
) {
    Crossfade(
        targetState = state is SearchScreenState.Loading,
        label = "",
    ) { targetState ->
        when (targetState) {
            true -> LoadingFiltersScreen(modifier = modifier)

            else -> {
                val successState = state as SearchScreenState.Success
                FiltersScreenContent(
                    modifier = modifier,
                    filtersList = successState.filtersList,
                    years = successState.years,
                )
            }
        }
    }
}

@Composable
private fun FiltersScreenContent(
    modifier: Modifier = Modifier,
    filtersList: List<Pair<String, List<StringResource>>>,
    years: List<Int>,
) {
    LazyColumn(
        contentPadding = LocalNavigationPadding.current
    ) {
        items(filtersList) { (title, items) ->
            SearchFilterChips(
                modifier = modifier,
                title = title,
                items = items,
            )
        }
        item {
            val minYear = years.first().toFloat()
            val maxYear = years.last().toFloat()
            var yearRange by remember { mutableStateOf(minYear..maxYear) }
            YearSlider(
                minValue = minYear,
                maxValue = maxYear,
                sliderPosition = yearRange,
                onValueChange = { newRange -> yearRange = newRange },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchFilterChips(
    modifier: Modifier = Modifier,
    title: String,
    items: List<StringResource>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                AssistChip(
                    onClick = {},
                    label = { Text(text = stringResource(item)) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearSlider(
    modifier: Modifier = Modifier,
    minValue: Float,
    maxValue: Float,
    steps: Int? = null,
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val calculatedSteps = steps ?: ((maxValue - minValue).toInt() - 1).coerceAtLeast(0)
    val rangeSliderState = remember {
        RangeSliderState(
            activeRangeStart = minValue,
            activeRangeEnd = maxValue,
            valueRange = minValue..maxValue,
            steps = calculatedSteps,
            onValueChangeFinished = {
                // TODO: передача финальных значений слайдера
            },
        )
    }
    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(
                text = minValue.toInt().toString(),
                modifier = Modifier.semantics { contentDescription = "Minimum Year" },
            )
            RangeSlider(
                state = rangeSliderState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
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
                                Text("%.0f".format(rangeSliderState.activeRangeStart))
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
                                Text("%.0f".format(rangeSliderState.activeRangeEnd))
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
            Text(
                text = maxValue.toInt().toString(),
                modifier = Modifier.semantics { contentDescription = "Maximum Year" },
            )
        }
    }
}

@Composable
private fun LoadingFiltersScreen(modifier: Modifier = Modifier) {
    Surface {
        Column(modifier = modifier) {}
    }
}
