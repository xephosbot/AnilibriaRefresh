package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.ui.utils.LocalShimmer
import com.xbot.anilibriarefresh.ui.utils.shimmerSafe

@Composable
fun TitleCardItem(
    modifier: Modifier = Modifier,
    title: Title?,
    onClick: (Title) -> Unit
) {
    Crossfade(
        targetState = title,
        label = "" //TODO: информативный label для перехода
    ) { state ->
        when (state) {
            null -> LoadingTitleCardItem(modifier)
            else -> TitleCardItemContent(modifier, state, onClick)
        }
    }
}

@Composable
private fun TitleCardItemContent(
    modifier: Modifier = Modifier,
    title: Title,
    onClick: (Title) -> Unit
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        PosterImage(
            modifier = modifier
                .height(TitleCardHeight)
                .aspectRatio(7f / 10f)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick(title) },
            poster = title.poster
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingTitleCardItem(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .height(TitleCardHeight)
            .aspectRatio(7f / 10f)
            .shimmerSafe(shimmer)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
    )
}

private val TitleCardHeight = 192.dp