package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
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
import com.xbot.anilibriarefresh.ui.utils.drawFadingEdge
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe
import com.xbot.domain.model.TitleModel

@Composable
fun TitlePagerItem(
    modifier: Modifier = Modifier,
    title: TitleModel?,
    onClick: (TitleModel) -> Unit = {}
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitlePagerContent(
                //modifier = modifier.drawFadingEdge()
            )
            else -> TitlePagerItemContent(
                //modifier = modifier.drawFadingEdge(),
                title = state,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun TitlePagerItemContent(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: (TitleModel) -> Unit,
) {
    BoxWithConstraints {
        if (maxWidth < 600.dp) {
            TitlePagerItemCompactLayout(
                modifier = modifier,
                title = title,
                onClick = { onClick(title) }
            )
        } else {
            TitlePagerItemLargeLayout(
                modifier = modifier,
                title = title,
                onClick = { onClick(title) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitlePagerItemCompactLayout(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(7f / 10f),
            poster = title.poster
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            PagerContent(title = title)
            Spacer(modifier = Modifier.height(8.dp))
            PagerButton(onClick = onClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TitlePagerItemLargeLayout(
    modifier: Modifier = Modifier,
    title: TitleModel,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(TitlePagerItemLargeLayoutHeight)
                .blur(15.dp),
            poster = title.poster
        ) {
            it.override(20, 20)
        }
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                PagerContent(title = title)
                Spacer(modifier = Modifier.height(8.dp))
                PagerButton(onClick = onClick)
            }
            PosterImage(
                modifier = Modifier
                    .height(TitlePagerItemLargeLayoutHeight)
                    .aspectRatio(7f / 10f),
                poster = title.poster
            )
        }
    }
}

@Composable
private fun PagerContent(
    modifier: Modifier = Modifier,
    title: TitleModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(0.7f))
    ) {
        //TODO: Use MaterialTheme.typography style
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
    BoxWithConstraints {
        if (maxWidth < 600.dp) {
            LoadingBoxPlaceholder(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(7f / 10f)
            )
        } else {
            LoadingBoxPlaceholder(
                modifier = modifier
                    .height(TitlePagerItemLargeLayoutHeight)
            )
        }
    }
}

@Composable
private fun LoadingBoxPlaceholder(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .shimmerSafe(shimmer)
            .background(Color.LightGray)
    )
}

private val TitlePagerItemLargeLayoutHeight = 350.dp

