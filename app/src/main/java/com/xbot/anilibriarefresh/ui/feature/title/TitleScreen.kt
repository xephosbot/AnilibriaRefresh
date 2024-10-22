package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.anilibriarefresh.ui.theme.FadeGradientColorStops
import com.xbot.anilibriarefresh.ui.utils.only
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.EpisodeModel
import com.xbot.domain.model.MemberModel
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleDetailModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val fadeGradientBrush = Brush.verticalGradient(colorStops = FadeGradientColorStops)

    Crossfade(targetState = state, label = "") { s ->
        when (s) {
            //TODO: Loading screen
            is TitleScreenState.Loading -> Surface(modifier = Modifier.fillMaxSize()) { }
            is TitleScreenState.Success -> {
                TitleScreenContent(
                    modifier = modifier,
                    title = s.title,
                    onClick = {},
                    paddingValues = paddingValues
                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
                    fadeGradient = fadeGradientBrush
                )
            }
        }
    }
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    onClick: () -> Unit,
    paddingValues: PaddingValues,
    fadeGradient: Brush
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues = paddingValues)
    ) {

        BoxTitleScreen(title = title, fadeGradient = fadeGradient)

//        Spacer(modifier.padding(16.dp))
        Text(
            text = title.name,
            modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier.padding(10.dp))
        Text(
            text = title.description,
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxTitleScreen(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    fadeGradient: Brush
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clipToBounds()
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .border(BorderStroke(1.dp, Color.Red)),
            poster = title.poster
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .background(fadeGradient)
        )
    }
}

@Preview
@Composable
private fun TitleScreenPreview() {
    val titleModel = TitleDetailModel(
        id = 1,
        type = "ТВ",
        year = 2020,
        name = "Целитель, которого исключили из группы, оказался сильнейшим",
        season = "Лето",
        description = "Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра. Но амбиций Вельтора это не остановит. Пусть пока что наследие некогда могущественного демона сводится лишь к нескольким параграфам в учебниках истории, заблуждаться не стоит: очень скоро этот дивный новый мир будет снова у его ног. Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра.",
        poster = PosterModel(
            src = null,
            thumbnail = null
        ),
        freshAt = "2019-12-29T23:06:39+00:00",
        createdAt = "2019-12-29T23:06:39+00:00",
        updatedAt = "2023-08-20T15:08:20+00:00",
        isOngoing = false,
        ageRating = "16+",
        publishDay = DayOfWeek.SUNDAY,
        notification = "Серии выходят по воскресеньям",
        episodesTotal = 34,
        isInProduction = false,
        addedInUsersFavorites = 35034,
        averageDurationOfEpisode = 25,
        genres = listOf("Приключения", "Фантастика", "Экшен", "Романтика"),
        members = listOf(
            MemberModel(
                id = "e3d555b0",
                name = "Sharon",
                role = "Озвучка"
            ),
            MemberModel(
                id = "e3d55444",
                name = "HectoR",
                role = "Озвучка"
            ),
            MemberModel(
                id = "e3d55dddd",
                name = "Flerion",
                role = "Перевод и адаптация"
            ),
            MemberModel(
                id = "e221111dd",
                name = "Kiota",
                role = "Оформление"
            )
        ),
        episodes = listOf(
            EpisodeModel(
                id = "95c359d0",
                name = "Андерворлд",
                duration = 2880,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                ordinal = 1
            ),
            EpisodeModel(
                id = "95c359d1",
                name = "Древо зла",
                duration = 1440,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                ordinal = 2
            ),
            EpisodeModel(
                id = "95c322d1",
                name = "Пограничный Хребет",
                duration = 1550,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                ordinal = 3
            ),
            EpisodeModel(
                id = "951159d1",
                name = "Отправление",
                duration = 1980,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                ordinal = 4
            ),
            EpisodeModel(
                id = "9511444000",
                name = "Океаническая Черепаха",
                duration = 2120,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                ordinal = 5
            ),
        )
    )
    TitleScreenContent(title = titleModel, paddingValues = PaddingValues(0.dp), onClick = {}, fadeGradient = Brush.verticalGradient(colorStops = FadeGradientColorStops))
}