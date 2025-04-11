package com.xbot.shared.ui.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.shared.ui.designsystem.modifier.LocalShimmer
import com.xbot.shared.ui.designsystem.modifier.scrim
import com.xbot.shared.ui.designsystem.modifier.shimmerSafe
import com.xbot.shared.domain.models.Release
import com.xbot.shared.ui.localization.localizedName

@Composable
fun ReleaseCardItem(
    release: Release?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: (Release) -> Unit,
) {
    Crossfade(
        targetState = release,
        label = "ReleaseCardItem Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
    ) { state ->
        when (state) {
            null -> LoadingReleaseCardItem(modifier, shape)
            else -> ReleaseCardItemContent(modifier, state, shape, onClick)
        }
    }
}

@Composable
private fun ReleaseCardItemContent(
    modifier: Modifier = Modifier,
    release: Release,
    shape: Shape,
    onClick: (Release) -> Unit,
) {
    Box(
        modifier = modifier
            .width(ReleaseCardWidth)
            .aspectRatio(7f / 10f)
            .clip(shape)
            .clickable { onClick(release) },
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxSize()
                .scrim(1.0f),
            poster = release.poster,
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = release.localizedName(),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingReleaseCardItem(
    modifier: Modifier = Modifier,
    shape: Shape
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .width(ReleaseCardWidth)
            .aspectRatio(7f / 10f)
            .clip(shape)
            .scrim(1.0f)
            .shimmerSafe(shimmer)
            .background(Color.LightGray),
    )
}

private val ReleaseCardWidth = 124.dp
