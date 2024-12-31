package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.modifiers.LocalShimmer
import com.xbot.designsystem.modifiers.fadedEdge
import com.xbot.designsystem.modifiers.shimmerSafe
import com.xbot.domain.models.Release

@Composable
fun ReleasePagerItem(
    release: Release?,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = release,
        label = "ReleasePagerItem Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
    ) { state ->
        when (state) {
            null -> LoadingReleasePagerItem(modifier)
            else -> ReleasePagerItemContent(modifier, state)
        }
    }
}

@Composable
private fun ReleasePagerItemContent(
    modifier: Modifier = Modifier,
    release: Release
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f),
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxSize()
                .fadedEdge(edgeHeightRatio = 1f),
            poster = release.poster
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = release.name,
                fontSize = 40.sp,
                lineHeight = 56.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = release.favoritesCount.toString(),
                fontSize = 14.sp,
                lineHeight = 18.sp,
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = {}) {
                Text(text = "Смотреть")
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun LoadingReleasePagerItem(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(7f / 10f)
            .shimmerSafe(shimmer)
            .fadedEdge(edgeHeightRatio = 1f)
            .background(Color.LightGray),
    )
}