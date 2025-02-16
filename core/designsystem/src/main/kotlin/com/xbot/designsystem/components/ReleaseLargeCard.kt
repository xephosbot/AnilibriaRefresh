package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.designsystem.R
import com.xbot.designsystem.effects.LocalShimmer
import com.xbot.designsystem.effects.fadedEdge
import com.xbot.designsystem.effects.shimmerSafe
import com.xbot.domain.models.Release

@Composable
fun ReleaseLargeCard(
    release: Release?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Crossfade(
        targetState = release,
        label = "ReleaseLargeCard Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
    ) { state ->
        when (state) {
            null -> LoadingReleaseLargeCard(modifier)
            else -> ReleaseLargeCardContent(modifier, state, content)
        }
    }
}

@Composable
private fun ReleaseLargeCardContent(
    modifier: Modifier = Modifier,
    release: Release,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(calculateAspectRatio()),
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .fillMaxSize()
                .fadedEdge(edgeHeight = 400.dp)
                .fadedEdge(edgeHeight = 250.dp, bottomEdge = false),
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
                text = stringResource(R.string.subtitle_release_template, release.year, release.episodesCount ?: 0, release.episodeDuration ?: 0, release.favoritesCount),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            content()

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LoadingReleaseLargeCard(
    modifier: Modifier = Modifier
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(calculateAspectRatio())
            .shimmerSafe(shimmer)
            .fadedEdge(edgeHeight = 400.dp)
            .fadedEdge(edgeHeight = 250.dp, bottomEdge = false)
            .background(Color.LightGray),
    )
}

@Composable
private fun calculateAspectRatio(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
): Float {
    if (windowSizeClass.isCompact()) return 7f / 10f

    return when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> 7f / 3f
        WindowHeightSizeClass.MEDIUM -> 7f / 4f
        WindowHeightSizeClass.EXPANDED -> 7f / 3f
        else -> 7f / 3f
    }
}

private fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT