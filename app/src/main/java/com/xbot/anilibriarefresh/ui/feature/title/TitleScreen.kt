package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

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
                    paddingValues = paddingValues.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
                    fadeGradient = fadeGradientBrush
                )
            }
        }
    }
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
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

        Spacer(modifier.padding(16.dp))
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
    title: TitleModel,
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
                .aspectRatio(7f / 10f),
            poster = title.poster
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .background(fadeGradient)
        )
        /*IconComponent(
            modifier = Modifier.padding(start = 16.dp, top = 56.dp),
            icon = Icons.AutoMirrored.Default.KeyboardArrowLeft
        ) { }*/
    }
}

@Preview
@Composable
private fun TitleScreenPreview() {
    val titleModel = TitleModel(
        id = 1,
        name = "Атака титанов",
        description = "Некогда жил могучий дракон, родившийся вместе с богами и сравнявшийся с ними по силе. Он прожил действительно долгую жизнь, но в итоге был побеждён героем. Моля о спокойном забвении, где его больше не потревожат мирские тревоги, он вдруг осознал, что оказался вовсе не в небытии, а в теле новорождённого мальчика. Подрастая в захолустной деревне, бывший дракон, а ныне паренёк по имени Долон, легко вписался в человеческое общество, приняв его законы и обычаи и полюбив своих новых родителей. Жизнь у него настала простая, полная насущных забот и обязанностей. Обычно он занимался земледелием и охотой, но как-то раз отправился исследовать болото, где познакомился с девушкой-змеёй по имени Селина. Долон и Селина, отправившаяся на поиски потенциального мужа, быстро сдружились, и отправились навстречу приключениям!",
        tags = listOf("2024", "TV", "Приключения"),
        poster = PosterModel(
            src = null,
            thumbnail = null
        ),
        uploadedTime = null
    )
    TitleScreenContent(title = titleModel, paddingValues = PaddingValues(0.dp), onClick = {}, fadeGradient = Brush.verticalGradient(colorStops = FadeGradientColorStops))
}