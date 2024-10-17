package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.valentinilk.shimmer.shimmer
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.feature.home.listAnime
import com.xbot.domain.model.TitleModel

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: TitleModel?,
    sizeListTitles: Int?
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitlePagerContent(modifier)
            else -> TitlePagerItemContent(modifier, state, sizeListTitles ?: 0) {
                //TODO: переход по клику на экран тайтла
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitlePagerItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    sizeListTitles: Int,
    onClick: () -> Unit,
) {
    val colorStops = arrayOf(
        0.0f to Color.Transparent,
        0.4f to Color.Transparent,
        1f to MaterialTheme.colorScheme.surface
    )

    Box {
        PosterImage(
            poster = title.poster,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f)
                .background(Brush.verticalGradient(colorStops = colorStops))
                .align(Alignment.BottomCenter)
        )
        Column(modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 80.dp)) {

            PagerContent(title = title)
            Spacer(Modifier.padding(bottom = 8.dp))
            PagerButton(onClick = onClick)
            ProgressBarElements(title = title, sizeList = sizeListTitles)
        }
    }
}

@Composable
private fun PagerContent(modifier: Modifier = Modifier, title: TitleModel) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(0.7f))
    ) {
        Text(
            text = title.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        ) {
            title.tags.subList(0, 3).forEachIndexed { index, tag ->
                Text(
                    text = tag,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                if (index != 2) Text(" • ")
            }
        }
    }
}

@Composable
private fun PagerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colorStops = arrayOf(
        0.0f to Color(0xFFE01938),
        0.7f to Color(0xFFFF3857),
        1f to Color(0xFFFF3857)
    )

    ButtonComponent(
        text = stringResource(R.string.text_pager_button),
        icon = Icons.Default.PlayArrow,
        onClick = onClick,
        colorStops = colorStops,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}


@Composable
fun ProgressBarElements(title: TitleModel, sizeList: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..sizeList) {
            if (i == title.id) {
                ProgressBarComponent(
                    color = Color.Red,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                )
            } else {
                ProgressBarComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingTitlePagerContent(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .then(
                if (shimmer != null) Modifier.shimmer(shimmer)
                else Modifier
            )
            .background(Color.LightGray)
    )
}

@Preview
@Composable
private fun PrevTitleItem123() {
    TitlePagerItemContent(title = listAnime[0], sizeListTitles = 1) {}

}