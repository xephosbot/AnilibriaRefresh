package com.xbot.anilibriarefresh.ui.feature.title

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.TitleDetail
import com.xbot.anilibriarefresh.models.toPosterUi
import com.xbot.anilibriarefresh.ui.components.Header
import com.xbot.anilibriarefresh.ui.components.HeaderDefaults
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.domain.models.EpisodeModel
import com.xbot.domain.models.MemberModel
import com.xbot.domain.models.PosterModel
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import org.koin.androidx.compose.koinViewModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel(),
    paddingValues: PaddingValues,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        paddingValues = paddingValues,
    )
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onAction: (TitleScreenAction) -> Unit,
    paddingValues: PaddingValues,
) {
    Crossfade(
        targetState = state,
        label = "TitleScreenContent Crossfade to ${state::class.simpleName}",
    ) { targetState ->
        when (targetState) {
            // TODO: Loading screen
            is TitleScreenState.Loading -> Box(modifier = Modifier.fillMaxSize())
            is TitleScreenState.Success -> {
                TitleDetail(
                    modifier = modifier,
                    title = targetState.title,
                    paddingValues = paddingValues,
                )
            }
        }
    }
}

@Composable
private fun TitleDetail(
    modifier: Modifier = Modifier,
    title: TitleDetail,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddingValues,
    ) {
        mainContent(
            title = title,
        )
        header(
            textId = R.string.genres_title,
        )
        horizontalItems(
            items = title.genres.map { it.name },
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) { genre ->
            AssistChip(
                onClick = {},
                label = { Text(text = genre) },
            )
        }
        header(
            textId = R.string.description_title,
            contentPadding = HeaderDefaults.ContentPaddingExcludeBottom,
        )
        description(
            text = title.description,
        )
        header(
            textId = R.string.episodes_title,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )
        verticalItems(
            items = title.episodes,
        ) { episode ->
            if (episode != null) {
                EpisodeItem(
                    episode = episode,
                    onClick = {},
                )
            }
        }
    }
}

private fun LazyListScope.mainContent(
    title: TitleDetail,
) {
    item(
        contentType = { "MainContent" },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            PosterImage(
                modifier = Modifier
                    .height(164.dp)
                    .aspectRatio(7f / 10f)
                    .clip(RoundedCornerShape(8.dp)),
                poster = title.poster,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    modifier: Modifier = Modifier,
    episode: EpisodeModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        PosterImage(
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            poster = episode.preview.toPosterUi(),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Серия ${episode.ordinal}" + if (episode.name != null) " • ${episode.name}" else "",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        )
    }
}

private fun LazyListScope.verticalItems(
    items: List<EpisodeModel>,
    itemContent: @Composable LazyItemScope.(EpisodeModel?) -> Unit,
) {
    items(
        items = items,
        key = { it.id },
    ) {
        itemContent(it)
    }
}

private fun LazyListScope.horizontalItems(
    items: List<String>,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable LazyItemScope.(String) -> Unit,
) {
    item(
        contentType = { "HorizontalList" },
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
        ) {
            items(
                items = items,
                key = { it },
            ) {
                itemContent(it)
            }
        }
    }
}

private fun LazyListScope.description(
    text: String,
) {
    item(
        contentType = { "Description" },
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                }
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                text = text,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
            )
        }
    }
}

private fun LazyListScope.header(
    @StringRes textId: Int,
    contentPadding: PaddingValues = HeaderDefaults.ContentPadding,
) {
    item(
        contentType = { "Header" },
    ) {
        Header(
            title = stringResource(textId),
            contentPadding = contentPadding,
        )
    }
}

@Preview
@Composable
private fun TitleScreenPreview() {
    val titleModel = TitleDetailModel(
        id = 1,
        type = ReleaseType.MOVIE,
        year = 2020,
        name = "Целитель, которого исключили из группы, оказался сильнейшим",
        season = Season.SUMMER,
        description = "Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра. Но амбиций Вельтора это не остановит. Пусть пока что наследие некогда могущественного демона сводится лишь к нескольким параграфам в учебниках истории, заблуждаться не стоит: очень скоро этот дивный новый мир будет снова у его ног. Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра.",
        poster = PosterModel(
            src = null,
            thumbnail = null,
        ),
        isOngoing = false,
        ageRating = AgeRating.R16_PLUS,
        publishDay = DayOfWeek.SUNDAY,
        notification = "Серии выходят по воскресеньям",
        episodesCount = 34,
        favoritesCount = 35034,
        episodeDuration = 25,
        genres = listOf(),
        members = listOf(
            MemberModel(
                id = "e3d555b0",
                name = "Sharon",
                role = "Озвучка",
            ),
            MemberModel(
                id = "e3d55444",
                name = "HectoR",
                role = "Озвучка",
            ),
            MemberModel(
                id = "e3d55dddd",
                name = "Flerion",
                role = "Перевод и адаптация",
            ),
            MemberModel(
                id = "e221111dd",
                name = "Kiota",
                role = "Оформление",
            ),
        ),
        episodes = listOf(
            EpisodeModel(
                id = "95c359d0",
                name = "Андерворлд",
                duration = 2880,
                preview = PosterModel(
                    src = null,
                    thumbnail = null,
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 1f,
            ),
            EpisodeModel(
                id = "95c359d1",
                name = "Древо зла",
                duration = 1440,
                preview = PosterModel(
                    src = null,
                    thumbnail = null,
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 2f,
            ),
            EpisodeModel(
                id = "95c322d1",
                name = "Пограничный Хребет",
                duration = 1550,
                preview = PosterModel(
                    src = null,
                    thumbnail = null,
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 3f,
            ),
            EpisodeModel(
                id = "951159d1",
                name = "Отправление",
                duration = 1980,
                preview = PosterModel(
                    src = null,
                    thumbnail = null,
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 4f,
            ),
            EpisodeModel(
                id = "9511444000",
                name = "Океаническая Черепаха",
                duration = 2120,
                preview = PosterModel(
                    src = null,
                    thumbnail = null,
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 5f,
            ),
        ),
    )
    // TitleDetail(title = titleModel, paddingValues = PaddingValues(0.dp))
}
