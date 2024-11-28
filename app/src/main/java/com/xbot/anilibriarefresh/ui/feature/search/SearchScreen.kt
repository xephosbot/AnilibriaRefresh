package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.ui.components.ButtonComponent
import com.xbot.anilibriarefresh.ui.components.Scaffold
import com.xbot.anilibriarefresh.ui.theme.colorStopsButtonPagerContent
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.anilibriarefresh.ui.utils.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val showBottomSheet: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val state by searchViewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        modifier = modifier,
        state = state,
        showBottomSheet = showBottomSheet,
        onAction = searchViewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    showBottomSheet: MutableState<Boolean>,
    onAction: (SearchScreenAction) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            SearchBar(
                showBottomSheet = showBottomSheet,
            )
            if (showBottomSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet.value = false
                    },
                ) {
                    Crossfade(
                        targetState = state is SearchScreenState.Loading,
                        label = "",
                    ) { targetState ->
                        when (targetState) {
                            true -> LoadingSearchScreen()
                            else -> {
                                val successState = state as SearchScreenState.Success
                                Filters(
                                    filtersList = successState.filtersList,
                                    years = successState.years,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>,
) {
    var textInput: String by rememberSaveable { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBar(
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textInput,
                    onQueryChange = { textInput = it },
                    expanded = false,
                    onExpandedChange = {},
                    onSearch = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "",
                        )
                    },
                    trailingIcon = {
                        Row {
                            IconButton(
                                onClick = {
                                    showBottomSheet.value = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "",
                                )
                            }
                            IconButton(
                                onClick = {
                                    textInput = ""
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "",
                                )
                            }
                        }
                    },
                )
            },
            expanded = false,
            onExpandedChange = {}
        ) {}
    }
}

@Composable
private fun Filters(
    modifier: Modifier = Modifier,
    filtersList: List<Pair<String, List<StringResource>>>,
    years: List<Int>,
) {
    LazyColumn {
        items(filtersList) { filter ->
            ItemsFilter(
                modifier = modifier,
                text = filter.first,
                list = filter.second,
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
            ButtonComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                text = "Поиск",
                onClick = {},
                colorStops = colorStopsButtonPagerContent,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemsFilter(
    modifier: Modifier = Modifier,
    text: String,
    list: List<StringResource>? = null,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    )
    if (list != null) {
        FlowRow(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            list.forEach { item ->
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

    ItemsFilter(text = "Год")
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
                            PlainTooltip(modifier = Modifier.sizeIn(45.dp, 25.dp).wrapContentWidth()) {
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
                                modifier = Modifier.sizeIn(45.dp, 25.dp).wrapContentWidth(),
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
fun LoadingSearchScreen(modifier: Modifier = Modifier) {
    Surface {
        Column(modifier = modifier) {}
    }
}
