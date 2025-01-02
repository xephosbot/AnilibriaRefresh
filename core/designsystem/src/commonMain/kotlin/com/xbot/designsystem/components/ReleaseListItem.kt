package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.modifiers.LocalShimmer
import com.xbot.designsystem.modifiers.shimmerSafe
import com.xbot.designsystem.resources.Res
import com.xbot.designsystem.resources.subtitle_release_template
import com.xbot.domain.models.Release
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReleaseListItem(
    release: Release?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceBright,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (Release) -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = ReleaseItemContainerPaddingHorizontal)
            .then(modifier),
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
    ReleaseItemLayout(
        modifier = modifier.height(ReleaseItemContainerHeight),
        headlineContent = {
            Text(
                text = release.name,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = release.description.lines().joinToString(" "),
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            PosterImage(release.poster)
        },
        tags = {
            Text(
                text = stringResource(Res.string.subtitle_release_template, release.year, release.episodesCount ?: 0, release.episodeDuration ?: 0, release.favoritesCount),
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

    ReleaseItemLayout(
        modifier = modifier
            .height(ReleaseItemContainerHeight)
            .shimmerSafe(shimmer),
        headlineContent = {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
            )
        },
        tags = {
            val tagsCount = 3
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
        },
    )
}

@Composable
private fun ReleaseItemLayout(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    leadingContent: @Composable () -> Unit,
    tags: @Composable RowScope.() -> Unit,
) {
    val headlineBox = @Composable {
        Box {
            // TODO: Use MaterialTheme.typography style
            ProvideTextStyle(
                value = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                ),
                content = headlineContent,
            )
        }
    }
    val supportingBox = @Composable {
        Box {
            // TODO: Use MaterialTheme.typography style
            ProvideTextStyle(
                value = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                content = supportingContent,
            )
        }
    }
    val tagsRow = @Composable {
        ProvideTextStyle(
            value = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ReleaseItemTagsSpacing),
                verticalAlignment = Alignment.CenterVertically,
                content = tags,
            )
        }
    }

    Layout(
        modifier = modifier,
        contents = listOf(headlineBox, supportingBox, leadingContent, tagsRow),
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
        val headlineHeight =
            (constraints.maxHeight - verticalPadding * 2 - tagsPlaceable.height - spacing).coerceAtLeast(
                0
            )

        val headlinePlaceable = headlineMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                    maxHeight = headlineHeight,
                ),
            )

        val headlineOffset = verticalPadding + headlinePlaceable.height + spacing
        val tagsOffset = headlineOffset + tagsPlaceable.height + spacing
        val supportingHeight =
            (constraints.maxHeight - tagsOffset - verticalPadding).coerceAtLeast(0)

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

fun listItemShape(index: Int, lastIndex: Int): Shape = when {
    index == 0 && index == lastIndex -> RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    index == 0 -> RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 4.dp,
        bottomEnd = 4.dp
    )

    index == lastIndex -> RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 4.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp,
    )

    else -> RoundedCornerShape(4.dp)
}

private val ReleaseItemContainerPaddingVertical = 16.dp
private val ReleaseItemContainerPaddingHorizontal = 16.dp
private val ReleaseItemContainerHeight = 160.dp
private val ReleaseItemContentSpacingVertical = 4.dp
private val ReleaseItemTagsSpacing = 8.dp
private val ReleaseItemMinContentSize = 16.dp
