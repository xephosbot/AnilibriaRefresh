package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.common.localization.localizedName
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
    BoxWithConstraints {
        val state = calculateLargeCardState(maxWidth)

        Crossfade(
            targetState = release,
        ) { targetState ->
            when (targetState) {
                null -> LoadingReleaseLargeCard(modifier, state)
                else -> ReleaseLargeCard(modifier, state, targetState, content)
            }
        }
    }

}

@Composable
private fun ReleaseLargeCard(
    modifier: Modifier = Modifier,
    state: LargeCardState,
    release: Release,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(state.containerHeight),
        contentAlignment = Alignment.BottomStart
    ) {
        PosterImage(
            modifier = Modifier
                .width(state.posterWidth)
                .fillMaxHeight()
                .fadedEdge(edgeHeight = 350.dp)
                .fadedEdge(edgeHeight = 250.dp, opacity = 0.7f, bottomEdge = false)
                .align(Alignment.BottomEnd),
            poster = release.poster
        )

        ReleaseLargeCardContent(
            modifier = Modifier
                .width(state.contentWidth)
                .align(Alignment.BottomStart),
            title = {
                Text(
                    text = release.localizedName(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            subtitle = {
                Text(
                    text = stringResource(R.string.subtitle_release_template, release.year, release.episodesCount ?: 0, release.episodeDuration ?: 0, release.favoritesCount),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            content = content
        )
    }
}

@Composable
private fun ReleaseLargeCardContent(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.displayMedium) {
            title()
        }
        Spacer(Modifier.height(4.dp))
        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
            Box(modifier = Modifier.graphicsLayer { alpha = 0.8f }) {
                subtitle()
            }
        }
        Spacer(Modifier.height(20.dp))
        content()
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun LoadingReleaseLargeCard(
    modifier: Modifier = Modifier,
    state: LargeCardState,
) {
    val shimmer = LocalShimmer.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(state.containerHeight)
            .shimmerSafe(shimmer)
            .fadedEdge(edgeHeight = 400.dp)
            .fadedEdge(edgeHeight = 250.dp, bottomEdge = false)
            .background(Color.LightGray),
    )
}

internal data class LargeCardState(
    val containerHeight: Dp,
    val contentWidth: Dp,
    val posterWidth: Dp
)

@Composable
private fun calculateLargeCardState(
    availableWidth: Dp,
    adaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
): LargeCardState {
    val heightSizeClass = adaptiveInfo.windowSizeClass.windowHeightSizeClass
    val widthSizeClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass

    val containerHeight = when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> Dp(availableWidth.value / 7f * 10f)
        widthSizeClass == WindowWidthSizeClass.MEDIUM &&
                heightSizeClass == WindowHeightSizeClass.COMPACT -> Dp(availableWidth.value / 7f * 2.75f)
        widthSizeClass == WindowWidthSizeClass.MEDIUM -> Dp(availableWidth.value / 7f * 4f)
        widthSizeClass == WindowWidthSizeClass.EXPANDED -> Dp(availableWidth.value / 7f * 3f)
        else -> Dp(availableWidth.value / 7f * 10f)
    }

    val contentWidth = when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> availableWidth
        widthSizeClass == WindowWidthSizeClass.MEDIUM -> Dp(availableWidth.value * 0.6f)
        widthSizeClass == WindowWidthSizeClass.EXPANDED -> Dp(availableWidth.value * 0.6f)
        else -> availableWidth
    }

    val posterWidth = when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> availableWidth
        widthSizeClass == WindowWidthSizeClass.MEDIUM -> Dp(availableWidth.value * 0.8f)
        widthSizeClass == WindowWidthSizeClass.EXPANDED -> Dp(availableWidth.value * 0.7f)
        else -> availableWidth
    }

    return remember(containerHeight, contentWidth, posterWidth) {
        LargeCardState(
            containerHeight = containerHeight,
            contentWidth = contentWidth,
            posterWidth = availableWidth,
        )
    }
}
