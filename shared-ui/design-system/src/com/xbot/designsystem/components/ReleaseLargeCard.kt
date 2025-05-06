package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.fadedEdge
import com.xbot.designsystem.modifier.shimmerSafe
import com.xbot.designsystem.utils.releaseTitleState
import com.xbot.domain.models.Release
import com.xbot.localization.localizedName

@Composable
fun ReleaseLargeCard(
    release: Release?,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    BoxWithConstraints {
        val width = maxWidth
        val height = calculateContainerHeight(width)
        val posterWidth = calculatePosterWidth(width)
        val contentWidth = calculateContentWidth(width)

        Crossfade(targetState = release) { state ->
            Column {
                when (state) {
                    null -> LoadingReleaseLargeCard(
                        height = height,
                        modifier = modifier.fillMaxWidth()
                    )
                    else -> ReleaseLargeCardContent(
                        release = state,
                        height = height,
                        posterWidth = posterWidth,
                        contentWidth = contentWidth,
                        modifier = modifier.fillMaxWidth(),
                        contentModifier = contentModifier
                    )
                }
                content?.let {
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        it()
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ReleaseLargeCardContent(
    release: Release,
    height: Dp,
    posterWidth: Dp,
    contentWidth: Dp,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
) {
    val releaseTitle by releaseTitleState(release)

    Box(
        modifier = Modifier
            .heightIn(max = height)
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .width(posterWidth)
                .fillMaxHeight()
                .fadedEdge(edgeHeight = 350.dp)
                .fadedEdge(edgeHeight = 200.dp, opacity = 0.7f, bottomEdge = false)
                .align(Alignment.BottomEnd)
                .clip(RectangleShape)
        ) {
            PosterImage(
                poster = release.poster,
                modifier = modifier.fillMaxSize()
            )
        }

        Column(
            modifier = contentModifier
                .width(contentWidth)
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.displayMedium) {
                Text(
                    text = release.localizedName(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(4.dp))
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(
                    text = releaseTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer { alpha = 0.8f }
                )
            }
        }
    }
}

@Composable
private fun LoadingReleaseLargeCard(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current
    Box(
        modifier = modifier
            .heightIn(max = height)
            .height(height)
            .shimmerSafe(shimmer)
            .fadedEdge(edgeHeight = 350.dp)
            .fadedEdge(edgeHeight = 200.dp, bottomEdge = false)
            .background(Color.LightGray)
    )
}


@Composable
private fun calculateContainerHeight(width: Dp): Dp {
    val info = currentWindowAdaptiveInfo()
    val widthClass = info.windowSizeClass.windowWidthSizeClass
    val heightClass = info.windowSizeClass.windowHeightSizeClass

    val rawHeight = when {
        widthClass == WindowWidthSizeClass.COMPACT -> width * 10f / 7f
        widthClass == WindowWidthSizeClass.MEDIUM && heightClass == WindowHeightSizeClass.COMPACT -> width * 2.75f / 7f
        else -> width * 4f / 7f
    }

    return if (widthClass == WindowWidthSizeClass.COMPACT) {
        rawHeight
    } else {
        rawHeight.coerceAtMost(400.dp)
    }
}

@Composable
private fun calculatePosterWidth(width: Dp): Dp {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    return when (widthClass) {
        WindowWidthSizeClass.COMPACT -> width
        WindowWidthSizeClass.MEDIUM -> width
        WindowWidthSizeClass.EXPANDED -> width
        else -> width
    }
}

@Composable
private fun calculateContentWidth(width: Dp): Dp {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    return when (widthClass) {
        WindowWidthSizeClass.COMPACT -> width
        else -> width * 0.6f
    }
}