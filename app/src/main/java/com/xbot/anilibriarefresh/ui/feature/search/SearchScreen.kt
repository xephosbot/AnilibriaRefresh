package com.xbot.anilibriarefresh.ui.feature.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RangeSlider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xbot.anilibriarefresh.ui.components.ButtonComponent
import com.xbot.anilibriarefresh.ui.theme.colorStopsButtonPagerContent

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val showBottomSheet: MutableState<Boolean> = remember { mutableStateOf(value = false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SearchBar(
                showBottomSheet = showBottomSheet
            )
            if (showBottomSheet.value) {
                Filters(
                    showBottomSheet = showBottomSheet,
                    allInfo = allInfo
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>) {
    var textInput: String by rememberSaveable { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textInput,
                    onQueryChange = {textInput = it},
                    expanded = false,
                    onExpandedChange = {},
                    onSearch = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        Row {
                            IconButton(
                                onClick = {
                                    showBottomSheet.value = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = ""
                                )
                            }
                            IconButton(
                                onClick = {
                                    textInput = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = {},
            windowInsets = WindowInsets(0.dp)
        ) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>,
    allInfo: AllInfo) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet.value = false
        },
        sheetState = sheetState
    ) {
        LazyColumn {
            item {
                ItemFilter(text = "Жанры", list = allInfo.genres)
                ItemFilter(text = "Возрастной рейтинг", list = allInfo.ageRating)
                ItemFilter(text = "Статус озвучки", list = allInfo.productionStatuses)
                ItemFilter(text = "Выход серий", list = allInfo.ongoingStatuses)
                ItemFilter(text = "Сезон", list = allInfo.seasons)
                ItemFilter(text = "Типы сортировки", list = allInfo.sortingTypes)
                ItemFilter(text = "Тип релиза", list = allInfo.typeRelease)
                TitleName(text = "Год")
                Slider()
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    text = "Поиск",
                    onClick = {},
                    colorStops = colorStopsButtonPagerContent)
            }
        }
    }
}

@Composable
fun ItemFilter(
    modifier: Modifier = Modifier,
    text: String,
    list: List<String>
) {
    TitleName(text = text)
    Items(list = list)
}

@Composable
fun TitleName(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Items(
    modifier: Modifier = Modifier,
    list: List<String>
) {
    FlowRow(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEach { item ->
            AssistChip(
                onClick = {},
                label = { Text(text = item) }
            )
        }
    }
}

@Composable
fun Slider(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableStateOf(0f..100f) }

    RangeSlider(
        modifier = modifier.padding(start = 32.dp, end = 32.dp),
        value = sliderPosition,
        steps = 10,
        onValueChange = {range -> sliderPosition = range},
        valueRange = 0f..100f,
        onValueChangeFinished = {},
    )
}

val allInfo = AllInfo(
    genres = listOf(
        "Боевые искусства",
        "Вампиры",
        "Гарем",
        "Демоны",
        "Детектив",
        "Дзёсей",
        "Драма",
        "Игры",
        "Исекай",
        "Исторический",
        "Киберпанк",
        "Комедия",
        "Магия"
    ),
    ageRating = listOf(
        "0+",
        "6+",
        "12+",
        "16+",
        "18+"
    ),
    productionStatuses = listOf(
        "Сейчас в озвучке",
        "Озвучка завершена"
    ),
    ongoingStatuses = listOf(
        "Онгоинг",
        "Неонгоинг"
    ),
    seasons = listOf(
        "Зима",
        "Весна",
        "Лето",
        "Осень"
    ),
    sortingTypes = listOf(
        "Обновлены недавно",
        "Обновлены давно",
        "Самый высокий рейтинг",
        "Самый низкий рейтинг",
        "Самые новые",
        "Самые старые"
    ),
    typeRelease = listOf(
        "ТВ",
        "ОNA",
        "WEB",
        "OVA",
        "OAD",
        "Фильм",
        "Дорама",
        "Спешл"
    ),
    years = listOf(
        "2015",
        "2016",
        "2017",
        "2018",
        "2019",
        "2020",
        "2021",
        "2022",
        "2023",
        "2024"
    )
)

data class AllInfo(
    val genres: List<String>,
    val ageRating: List<String>,
    val productionStatuses: List<String>,
    val ongoingStatuses: List<String>,
    val seasons: List<String>,
    val sortingTypes: List<String>,
    val typeRelease: List<String>,
    val years: List<String>
)