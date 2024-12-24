package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.modifiers.LocalShimmer
import com.xbot.designsystem.modifiers.shimmerSafe
import com.xbot.domain.models.Release

@Composable
fun TitleCardItem(
    modifier: Modifier = Modifier,
    release: Release?,
    onClick: (Release) -> Unit,
) {
    Crossfade(
        targetState = release,
        label = "TitleCardItem Crossfade to ${if (release == null) "Loading" else "Loaded Title"}",
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
    release: Release,
    onClick: (Release) -> Unit,
) {
    PosterImage(
        modifier = modifier
            .height(TitleCardHeight)
            .aspectRatio(7f / 10f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(release) },
        poster = release.poster,
    )
}

@Composable
private fun LoadingTitleCardItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .height(TitleCardHeight)
            .aspectRatio(7f / 10f)
            .shimmerSafe(shimmer)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
    )
}

private val TitleCardHeight = 192.dp
