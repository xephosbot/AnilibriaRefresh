package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.resources.Res
import com.xbot.resources.episode_abbreviation
import com.xbot.localization.localizedName
import com.xbot.localization.stringRes
import com.xbot.resources.minutes_abbreviation
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

@Composable
fun ReleaseLargeCard(
    release: Release?,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    BoxWithConstraints {
        val state = calculateLargeCardState(maxWidth)

        Crossfade(
            targetState = release,
        ) { targetState ->
            when (targetState) {
                null -> LoadingReleaseLargeCard(modifier, state, content)
                else -> ReleaseLargeCard(modifier, contentModifier, state, targetState, content)
            }
        }
    }
}

@Composable
private fun ReleaseLargeCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LargeCardState,
    release: Release,
    content: @Composable (() -> Unit)?
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(state.containerHeight),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .width(state.posterWidth)
                    .fillMaxHeight()
                    .fadedEdge(edgeHeight = 350.dp)
                    .fadedEdge(edgeHeight = 200.dp, opacity = 0.7f, bottomEdge = false)
                    .align(Alignment.BottomEnd)
                    .clip(RectangleShape)
            ) {
                PosterImage(
                    modifier = modifier.fillMaxSize(),
                    poster = release.poster
                )
            }

            val releaseTitle = remember {
                runBlocking { buildReleaseTitle(release) }
            }

            ReleaseLargeCardContent(
                modifier = contentModifier
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
                        text = releaseTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            )
        }
        if (content != null) {
            Spacer(Modifier.height(20.dp))
            Box(Modifier.padding(horizontal = 16.dp)) {
                content()
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ReleaseLargeCardContent(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.displayMedium) {
            title()
        }
        Spacer(Modifier.height(4.dp))
        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
            Box(modifier = Modifier.graphicsLayer {
                alpha = 0.8f
                clip = false
            }) {
                subtitle()
            }
        }
    }
}

@Composable
private fun LoadingReleaseLargeCard(
    modifier: Modifier = Modifier,
    state: LargeCardState,
    content: @Composable (() -> Unit)?
) {
    val shimmer = LocalShimmer.current

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(state.containerHeight)
                .shimmerSafe(shimmer)
                .fadedEdge(edgeHeight = 400.dp)
                .fadedEdge(edgeHeight = 250.dp, bottomEdge = false)
                .background(Color.LightGray),
        )
        if (content != null) {
            Spacer(Modifier.height(20.dp))
            Box(Modifier.padding(horizontal = 16.dp)) {
                content()
            }
        }
        Spacer(Modifier.height(8.dp))
    }
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

internal suspend fun buildReleaseTitle(release: Release) = buildString {
    append(release.year.toString())
    append(" \u2022 ")
    release.type?.let { type ->
        when (type) {
            ReleaseType.MOVIE -> {
                append(getString(type.stringRes))
                append(" \u2022 ")
            }

            else -> {
                release.episodesCount?.let { episodesCount ->
                    append(getString(Res.string.episode_abbreviation, episodesCount.toString()))
                    append(" \u2022 ")
                }
            }
        }
    }
    release.episodeDuration?.let { episodeDuration ->
        append(getString(Res.string.minutes_abbreviation, episodeDuration.toString()))
        append(" \u2022 ")
    }
    append(release.favoritesCount.toString())
    append(" \u2605")
}