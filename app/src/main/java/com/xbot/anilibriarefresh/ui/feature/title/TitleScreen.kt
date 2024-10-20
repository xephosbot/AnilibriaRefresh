package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.ui.components.PosterImage
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

    Crossfade(targetState = state) { s ->
        when (s) {
            //TODO: Loading screen
            is TitleScreenState.Loading -> Surface(modifier = Modifier.fillMaxSize()) { }
            is TitleScreenState.Success -> {
                TitleScreenContent(
                    modifier = modifier,
                    title = s.title,
                    onClick = {},
                    paddingValues = paddingValues.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues = paddingValues)
            .verticalScroll(rememberScrollState())) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(8.dp)),
            poster = title.poster
        )
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
    TitleScreenContent(title = titleModel, paddingValues = PaddingValues(0.dp), onClick = {})
}