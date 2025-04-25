package com.xbot.shared.ui.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.shared.domain.models.Release
import com.xbot.shared.domain.models.enums.ReleaseType
import com.xbot.shared.resources.Res
import com.xbot.shared.resources.episode_abbreviation
import com.xbot.shared.resources.minutes_abbreviation
import com.xbot.shared.ui.designsystem.modifier.LocalShimmer
import com.xbot.shared.ui.designsystem.modifier.cinematicScrim
import com.xbot.shared.ui.designsystem.modifier.fadedEdge
import com.xbot.shared.ui.designsystem.modifier.shimmerSafe
import com.xbot.shared.ui.localization.localizedName
import com.xbot.shared.ui.localization.stringRes
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.math.roundToInt

@Composable
fun ReleaseLargeCard(
    release: Release?,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Crossfade(
        targetState = release,
    ) { targetState ->
        when (targetState) {
            null -> LoadingReleaseLargeCard(modifier, content)
            else -> ReleaseLargeCardContent(modifier, contentModifier, targetState, content)
        }
    }
}

@Composable
private fun ReleaseLargeCardContent(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    release: Release,
    content: @Composable ColumnScope.() -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val heightSizeClass = adaptiveInfo.windowSizeClass.windowHeightSizeClass
    val widthSizeClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass
    val layoutType = when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> ReleaseLayoutType.COMPACT
        widthSizeClass == WindowWidthSizeClass.MEDIUM &&
                heightSizeClass == WindowHeightSizeClass.COMPACT -> ReleaseLayoutType.EXPANDED

        widthSizeClass == WindowWidthSizeClass.MEDIUM -> ReleaseLayoutType.MEDIUM
        widthSizeClass == WindowWidthSizeClass.EXPANDED -> ReleaseLayoutType.EXPANDED
        else -> ReleaseLayoutType.COMPACT
    }

    val releaseTitle = remember {
        runBlocking { buildReleaseTitle(release) }
    }

    ReleaseLargeCardLayout(
        modifier = modifier,
        contentModifier = contentModifier,
        layoutType = layoutType,
        poster = {
            PosterImage(
                modifier = Modifier.fillMaxSize(),
                poster = release.poster
            )
        },
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
        },
        description = {
            Text(
                text = release.description?.lines()?.joinToString(" ").orEmpty(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        content = content
    )
}

@Composable
private fun LoadingReleaseLargeCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val heightSizeClass = adaptiveInfo.windowSizeClass.windowHeightSizeClass
    val widthSizeClass = adaptiveInfo.windowSizeClass.windowWidthSizeClass
    val layoutType = when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> ReleaseLayoutType.COMPACT
        widthSizeClass == WindowWidthSizeClass.MEDIUM &&
                heightSizeClass == WindowHeightSizeClass.COMPACT -> ReleaseLayoutType.EXPANDED

        widthSizeClass == WindowWidthSizeClass.MEDIUM -> ReleaseLayoutType.MEDIUM
        widthSizeClass == WindowWidthSizeClass.EXPANDED -> ReleaseLayoutType.EXPANDED
        else -> ReleaseLayoutType.COMPACT
    }

    val shimmer = LocalShimmer.current

    ReleaseLargeCardLayout(
        layoutType = layoutType,
        poster = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmerSafe(shimmer)
                    .background(Color.LightGray)
            )
        },
        title = {},
        subtitle = {},
        description = {},
        content = content
    )
}

@Composable
private fun ReleaseLargeCardLayout(
    layoutType: ReleaseLayoutType,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    poster: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    description: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val isCompact = layoutType == ReleaseLayoutType.COMPACT

    val posterBox = @Composable {
        val fadeModifier = if (isCompact) {
            Modifier
                .fadedEdge(edgeHeight = 350.dp)
                .fadedEdge(edgeHeight = 200.dp, opacity = 0.7f, bottomEdge = false)
        } else {
            Modifier.cinematicScrim()
        }

        Box(
            modifier = fadeModifier
                .clip(RectangleShape)
                .then(modifier)
        ) {
            poster()
        }
    }
    val titleBox = @Composable {
        ProvideTextStyle(MaterialTheme.typography.displayMedium) {
            Box(modifier = contentModifier) {
                title()
            }
        }
    }
    val subtitleBox = @Composable {
        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
            Box(modifier = contentModifier.graphicsLayer { alpha = 0.8f }) {
                subtitle()
            }
        }
    }
    val descriptionBox = @Composable {
        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
            Box(modifier = contentModifier.graphicsLayer { alpha = 0.8f }) {
                description()
            }
        }
    }
    val contentColumn = @Composable {
        Column(
            modifier = contentModifier,
            content = content
        )
    }

    Layout(
        contents = listOf(posterBox, titleBox, subtitleBox, descriptionBox, contentColumn)
    ) { (posterMeasurable, titleMeasurable, subtitleMeasurable, descriptionMeasurable, contentMeasurable), constraints ->
        var layoutHeight = when (layoutType) {
            ReleaseLayoutType.COMPACT -> constraints.maxWidth / 7 * 10
            ReleaseLayoutType.MEDIUM -> constraints.maxWidth / 7 * 4
            ReleaseLayoutType.EXPANDED -> constraints.maxWidth / 7 * 3
        }
        val posterWidth = when (layoutType) {
            ReleaseLayoutType.COMPACT -> constraints.maxWidth
            ReleaseLayoutType.MEDIUM -> layoutHeight / 9 * 16
            ReleaseLayoutType.EXPANDED -> layoutHeight / 9 * 19
        }
        val padding = ContainerPaddingHorizontal.roundToPx()
        val paddingBottom = ContainerPaddingBottom.roundToPx()
        val spacing = ContentSpacingVertical.roundToPx()

        val posterPlaceable = posterMeasurable.first()
            .measure(
                constraints.copy(
                    maxWidth = posterWidth.coerceIn(0, constraints.maxWidth),
                    maxHeight = layoutHeight
                )
            )

        val contentWidth = when (layoutType) {
            ReleaseLayoutType.COMPACT -> constraints.maxWidth - padding * 2
            ReleaseLayoutType.MEDIUM -> constraints.maxWidth / 10 * 6
            ReleaseLayoutType.EXPANDED -> constraints.maxWidth / 10 * 6
        }
        val contentPlaceable = contentMeasurable.first()
            .measure(
                constraints.copy(
                    maxWidth = contentWidth
                )
            )

        val availableContentHeight = (if (isCompact) layoutHeight else layoutHeight - contentPlaceable.height - padding) * 0.6f
        layoutHeight += if (isCompact) contentPlaceable.height + padding + paddingBottom else 0

        val subtitlePlaceable = subtitleMeasurable.first()
            .measure(
                constraints.copy(
                    maxWidth = contentWidth
                )
            )

        val descriptionPlaceable = descriptionMeasurable.first()
            .measure(
                constraints.copy(
                    maxWidth = contentWidth
                )
            )

        val titlePlaceable = titleMeasurable.first()
            .measure(
                constraints.copy(
                    maxWidth = contentWidth,
                    maxHeight = (availableContentHeight.roundToInt() - subtitlePlaceable.height - spacing - descriptionPlaceable.height).coerceAtLeast(0)
                )
            )

        if (isCompact) {
            layout(constraints.maxWidth, layoutHeight) {
                posterPlaceable.placeRelative(0, 0)
                subtitlePlaceable.placeRelative(padding, posterPlaceable.height - subtitlePlaceable.height)
                titlePlaceable.placeRelative(padding, posterPlaceable.height - subtitlePlaceable.height - titlePlaceable.height - spacing)
                contentPlaceable.placeRelative(padding, layoutHeight - contentPlaceable.height - paddingBottom)
            }
        } else {
            layout(constraints.maxWidth, layoutHeight) {
                posterPlaceable.placeRelative(constraints.maxWidth - posterWidth, 0)
                descriptionPlaceable.placeRelative(padding, posterPlaceable.height - descriptionPlaceable.height - contentPlaceable.height - padding)
                titlePlaceable.placeRelative(padding, posterPlaceable.height - descriptionPlaceable.height - contentPlaceable.height - padding - titlePlaceable.height - spacing)
                subtitlePlaceable.placeRelative(padding, posterPlaceable.height - descriptionPlaceable.height - contentPlaceable.height - padding - titlePlaceable.height - subtitlePlaceable.height - spacing)
                contentPlaceable.placeRelative(padding, layoutHeight - contentPlaceable.height)
            }
        }
    }
}

/**
 * Types of release card layouts
 */
private enum class ReleaseLayoutType {
    COMPACT,
    MEDIUM,
    EXPANDED
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

private val ContainerPaddingHorizontal = 16.dp
private val ContainerPaddingBottom = 8.dp
private val ContentSpacingVertical = 4.dp