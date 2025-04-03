package com.xbot.designsystem.components

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.xbot.common.localization.localizedName
import com.xbot.common.localization.parseAsHtml
import com.xbot.common.localization.stringRes
import com.xbot.designsystem.R
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.shimmerSafe
import com.xbot.designsystem.utils.withRoundedCorner
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.ReleaseType

@Composable
fun ReleaseListItem(
    release: Release?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceBright,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: (Release) -> Unit = {},
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor
    ) {
        Crossfade(
            modifier = Modifier
                .clickable { release?.let { onClick(it) } },
            targetState = release,
            label = "ReleaseListItem Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
        ) { state ->
            when (state) {
                null -> LoadingReleaseListItem()
                else -> ReleaseListItemContent(state)
            }
        }
    }
}

@Composable
private fun ReleaseListItemContent(
    release: Release,
    modifier: Modifier = Modifier,
) {
    ListItemLayout(
        modifier = modifier.height(ReleaseItemContainerHeight),
        headlineContent = {
            Text(
                text = release.localizedName().parseAsHtml(),
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = release.description?.lines()?.joinToString(" ").orEmpty(),
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            PosterImage(release.poster)
        },
        tags = {
            Text(
                text = buildReleaseTitle(release, LocalContext.current),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
    )
}

@Composable
private fun LoadingReleaseListItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    ListItemLayout(
        modifier = modifier
            .height(ReleaseItemContainerHeight)
            .shimmerSafe(shimmer),
        headlineContent = {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
            )
        },
        supportingContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray),
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .background(Color.LightGray),
            )
        },
        tags = {
            val tagsCount = 3
            Row(
                horizontalArrangement = Arrangement.spacedBy(ReleaseItemTagsSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(tagsCount) { index ->
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray),
                    )
                    if (index != tagsCount - 1) Text("•")
                }
            }
        },
    )
}

@Composable
private fun ListItemLayout(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    leadingContent: @Composable () -> Unit,
    tags: @Composable () -> Unit,
) {
    val headlineBox = @Composable {
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyLarge,
            content = headlineContent,
        )
    }
    val supportingBox = @Composable {
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Box(modifier = Modifier.graphicsLayer { alpha = DescriptionAlpha }) {
                supportingContent.invoke()
            }
        }
    }
    val tagsBox = @Composable {
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Box(modifier = Modifier.graphicsLayer { alpha = SubtitleAlpha }) {
                tags.invoke()
            }
        }
    }

    Layout(
        modifier = modifier,
        contents = listOf(headlineBox, supportingBox, leadingContent, tagsBox),
    ) { (headlineMeasurable, supportingMeasurable, leadingMeasurable, tagsMeasurable), constraints ->
        val leadingWidth = constraints.maxHeight * 7 / 10
        val leadingPlaceable = leadingMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    minWidth = leadingWidth,
                    maxWidth = leadingWidth,
                ),
            )

        val leadingPadding = ReleaseItemContainerPaddingHorizontal.roundToPx()
        val contentWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - leadingPlaceable.width - leadingPadding * 2).coerceAtLeast(0)
        }

        val tagsPlaceable = tagsMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                ),
            )

        val spacing = ReleaseItemContentSpacingVertical.roundToPx()
        val verticalPadding = ReleaseItemContainerPaddingVertical.roundToPx()
        val doubleVerticalPadding = (ReleaseItemContainerPaddingVertical * 2).roundToPx()
        val headlineHeight =
            (constraints.maxHeight - doubleVerticalPadding - tagsPlaceable.height).coerceAtLeast(0)

        val headlinePlaceable = headlineMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                    maxHeight = headlineHeight,
                ),
            )

        val headlineOffset = verticalPadding + headlinePlaceable.height
        val tagsOffset = headlineOffset + tagsPlaceable.height + spacing
        val supportingHeight =
            (constraints.maxHeight - doubleVerticalPadding - headlinePlaceable.height - tagsPlaceable.height - spacing).coerceAtLeast(0)

        val supportingPlaceable = supportingMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                    maxHeight = supportingHeight,
                ),
            )

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            leadingPlaceable.placeRelative(
                x = 0,
                y = 0,
            )
            headlinePlaceable.placeRelative(
                x = leadingWidth + leadingPadding,
                y = verticalPadding,
            )
            tagsPlaceable.placeRelative(
                x = leadingWidth + leadingPadding,
                y = headlineOffset,
            )
            // Place content only if it has a size of at least 1 line.
            if (supportingPlaceable.height > ReleaseItemMinContentSize.roundToPx()) {
                supportingPlaceable.placeRelative(
                    x = leadingWidth + leadingPadding,
                    y = tagsOffset,
                )
            }
        }
    }
}

private val ReleaseItemContainerPaddingVertical = 12.dp
private val ReleaseItemContainerPaddingHorizontal = 16.dp
private val ReleaseItemContainerHeight = 160.dp
private val ReleaseItemContentSpacingVertical = 4.dp
private val ReleaseItemTagsSpacing = 8.dp
private val ReleaseItemMinContentSize = 16.dp
private const val SubtitleAlpha = 0.6f
private const val DescriptionAlpha = 0.8f