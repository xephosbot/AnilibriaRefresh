package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.theme.colorStopsButtonPagerContent
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe
import com.xbot.domain.model.TitleModel

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: TitleModel?,
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitlePagerContent(modifier)
            else -> TitlePagerItemContent(modifier, state) {
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
    onClick: () -> Unit,
) {
    val fadeGradientBrush = Brush.verticalGradient(colorStops = FadeGradientColorStops)

    Box(
        modifier = Modifier
            .wrapContentSize()
            .heightIn(max = TitlePagerItemMaxHeight)
            .clipToBounds()
    ) {
        PosterImage(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f),
            poster = title.poster
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fadeGradientBrush)
        )
        Column(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            PagerContent(title = title)
            Spacer(Modifier.padding(bottom = 8.dp))
            PagerButton(onClick = onClick)
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
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally),
            text = title.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center,
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        ) {
            title.tags.forEachIndexed { index, tag ->
                Text(
                    text = tag,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                if (index != title.tags.lastIndex) Text(" • ")
            }
        }
    }
}

@Composable
private fun PagerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ButtonComponent(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clip(RoundedCornerShape(12.dp)),
        text = stringResource(R.string.text_pager_button),
        icon = Icons.Default.PlayArrow,
        onClick = onClick,
        colorStops = colorStopsButtonPagerContent
    )
}

@Composable
private fun LoadingTitlePagerContent(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .heightIn(max = TitlePagerItemMaxHeight)
            .clipToBounds()
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .shimmerSafe(shimmer)
            .background(Color.LightGray)
    )
}

private val FadeGradientColorStops
    @Composable
    get() = arrayOf(
        0.0f to Color.Transparent,
        0.4f to Color.Transparent,
        1f to MaterialTheme.colorScheme.surface
    )

private val TitlePagerItemMaxHeight = 400.dp
