package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.fadingEdge
import com.xbot.designsystem.modifier.marquee
import com.xbot.designsystem.theme.LocalMargins
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.models.Release
import com.xbot.formatters.localizedName
import com.xbot.resources.Res
import com.xbot.resources.button_watch
import org.jetbrains.compose.resources.stringResource

@Composable
fun LargeReleaseCard(
    release: Release?,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal =
            LocalMargins.current.horizontal + 8.dp
    ),
    content: @Composable (ColumnScope.(contentAlignment: Alignment.Horizontal) -> Unit)? = null
) {
    Crossfade(targetState = release != null) { isLoaded ->
        if (isLoaded) {
            release?.let {
                LargeReleaseCardContent(
                    modifier = modifier,
                    contentModifier = contentModifier,
                    contentPadding = contentPadding,
                    release = it,
                    content = content
                )
            }
        } else {
            LargeReleaseCardPlaceholder(
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
private fun LargeReleaseCardContent(
    release: Release,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    content: @Composable (ColumnScope.(contentAlignment: Alignment.Horizontal) -> Unit)?
) {
    LargeReleaseCardLayout(
        modifier = Modifier,
        contentModifier = contentModifier,
        contentPadding = contentPadding,
        poster = {
            PosterImage(
                poster = release.poster,
                modifier = modifier.fillMaxSize()
            )
        },
        content = { contentAlignment ->
            ReleaseMetaText(
                modifier = Modifier.marquee(),
                release = release
            )
            TextAutoSize(
                modifier = Modifier.fillMaxWidth(),
                text = release.localizedName(),
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = MaterialTheme.typography.displayMedium.fontSize,
                    minFontSize = MaterialTheme.typography.headlineLarge.fontSize
                ),
                style = MaterialTheme.typography.displayMedium
                    .copy(
                        lineBreak = LineBreak.Paragraph,
                        hyphens = Hyphens.Auto
                    ),
                textAlign = when (contentAlignment) {
                    Alignment.Start -> TextAlign.Start
                    else -> TextAlign.Center
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            content?.invoke(this, contentAlignment)
        }
    )
}

@Composable
private fun LargeReleaseCardLayout(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    poster: @Composable () -> Unit,
    content: @Composable ColumnScope.(Alignment.Horizontal) -> Unit
) {
    BoxWithConstraints {
        val windowHeight = with(LocalDensity.current) {
            LocalWindowInfo.current.containerSize.height.toDp()
        }
        val availableHeight = if (constraints.hasBoundedHeight) maxHeight else windowHeight
        val metrics = largeReleaseCardMetrics(maxWidth, availableHeight)
        val ratio = if (metrics.height > 0.dp) maxWidth / metrics.height else 1f
        val contentAlignment = metrics.contentAlignment

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
                .then(modifier),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .fadingEdge(
                        startFraction = 0.25f,
                        endFraction = 0.75f
                    )
            ) {
                poster()
            }

            Column(
                modifier = contentModifier
                    .width(metrics.contentWidth)
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = contentAlignment,
                content = { content(contentAlignment) }
            )
        }
    }
}

@Composable
private fun LargeReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val shimmer = LocalShimmer.current

    LargeReleaseCardLayout(
        modifier = Modifier,
        contentPadding = contentPadding,
        poster = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .background(Color.LightGray)
            )
        },
        content = {}
    )
}

@AnilibertyPreview
@Composable
private fun LargeReleaseCardPreview() {
    val release = ReleaseFixtures.all[3]
    LargeReleaseCard(
        release = release
    ) { contentAlignment ->
        release.description?.let { description ->
            Text(
                text = description.lines().joinToString(" "),
                style = MaterialTheme.typography.bodySmall,
                textAlign = when (contentAlignment) {
                    Alignment.Start -> TextAlign.Start
                    else -> TextAlign.Center
                },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        MediumSplitButton(
            onLeadingClick = {
                // Handle leading button click
            },
            onTrailingClick = {
                // Handle trailing button click
            },
            leadingContent = {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                    imageVector = com.xbot.designsystem.icons.AnilibertyIcons.Filled.PlayArrow,
                    contentDescription = null
                )
                Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                Text(
                    text = stringResource(Res.string.button_watch),
                    maxLines = 1
                )
            },
            trailingContent = {
                Icon(
                    modifier = Modifier.size(SplitButtonDefaults.MediumTrailingButtonIconSize),
                    imageVector = com.xbot.designsystem.icons.AnilibertyIcons.MoreVert,
                    contentDescription = null
                )
            }
        )
    }
}

@Immutable
private data class LargeReleaseCardMetrics(
    val height: Dp,
    val contentWidth: Dp,
    val contentAlignment: Alignment.Horizontal
)

private fun largeReleaseCardMetrics(width: Dp, availableHeight: Dp): LargeReleaseCardMetrics =
    if (width < WIDTH_DP_MEDIUM_LOWER_BOUND.dp) {
        LargeReleaseCardMetrics(
            height = width * 10f / 7f,
            contentWidth = width,
            contentAlignment = Alignment.CenterHorizontally
        )
    } else {
        val heightRatio = if (availableHeight >= HEIGHT_DP_MEDIUM_LOWER_BOUND.dp) 4f else 2.75f
        LargeReleaseCardMetrics(
            height = (width * heightRatio / 7f).coerceAtMost(400.dp),
            contentWidth = if (width >= WIDTH_DP_EXPANDED_LOWER_BOUND.dp) 500.dp else width * 0.6f,
            contentAlignment = Alignment.Start
        )
    }
