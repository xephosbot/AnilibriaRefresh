package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let {
        TitleScreenContent(
            modifier = modifier,
            title = it,
            onClick = {}
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)) {
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
        description = "Аниме об уничтожении мира, где главный герой может уничтожить весь мир и не хочет чтобы его друзья погибали",
        poster = PosterModel(
            src = null,
            thumbnail = null
        ),
        uploadedTime = null
    )
    TitleScreenContent(title = titleModel) {}
}