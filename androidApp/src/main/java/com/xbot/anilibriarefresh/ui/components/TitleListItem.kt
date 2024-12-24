package com.xbot.anilibriarefresh.ui.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.modifiers.LocalShimmer
import com.xbot.designsystem.modifiers.shimmerSafe
import com.xbot.domain.models.Release

@Composable
fun TitleListItem(
    modifier: Modifier = Modifier,
    release: Release?,
    onClick: (Release) -> Unit = {},
) {
    Crossfade(
        modifier = Modifier
            .clickable { release?.let { onClick(it) } },
        targetState = release,
        label = "TitleListItem Crossfade to ${if (release == null) "Loading" else "Loaded Title"}",
    ) { state ->
        when (state) {
            null -> LoadingTitleListItem(modifier)
            else -> TitleListItemContent(modifier, state)
        }
    }
}

@Composable
private fun TitleListItemContent(
    modifier: Modifier = Modifier,
    release: Release,
) {
    TitleItemLayout(
        modifier = modifier
            .height(TitleItemContainerHeight)
            .padding(
                vertical = TitleItemContainerPaddingVertical,
                horizontal = TitleItemContainerPaddingHorizontal
            ),
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
            PosterImage(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                poster = release.poster,
            )
        },
        tags = {

        },
    )
}

@Composable
private fun LoadingTitleListItem(
    modifier: Modifier = Modifier,
) {
    val shimmer = LocalShimmer.current

    TitleItemLayout(
        modifier = modifier
            .height(TitleItemContainerHeight)
            .padding(
                vertical = TitleItemContainerPaddingVertical,
                horizontal = TitleItemContainerPaddingHorizontal
            )
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
private fun TitleItemLayout(
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
                value = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
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
                ),
                content = supportingContent,
            )
        }
    }
    val tagsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(TitleItemTagsSpacing),
            verticalAlignment = Alignment.CenterVertically,
            content = tags,
        )
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

        val leadingPadding = TitleItemContainerPaddingHorizontal.roundToPx()
        val contentWidth = if (constraints.maxWidth == Constraints.Infinity) {
            constraints.maxWidth
        } else {
            (constraints.maxWidth - leadingPlaceable.width - leadingPadding).coerceAtLeast(0)
        }

        val tagsPlaceable = tagsMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                ),
            )

        val headlinePadding = TitleItemContentPadding.roundToPx()
        val headlineHeight =
            (constraints.maxHeight - tagsPlaceable.height - headlinePadding).coerceAtLeast(0)

        val headlinePlaceable = headlineMeasurable.first()
            .measure(
                constraints = constraints.copy(
                    maxWidth = contentWidth,
                    minHeight = 0,
                    maxHeight = headlineHeight,
                ),
            )

        val headlineOffset = headlinePlaceable.height + headlinePadding
        val tagsOffset = headlineOffset + tagsPlaceable.height + headlinePadding
        val supportingHeight = (constraints.maxHeight - tagsOffset).coerceAtLeast(0)

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
                y = 0,
            )
            tagsPlaceable.placeRelative(
                x = leadingWidth + leadingPadding,
                y = headlineOffset,
            )
            // Place content only if it has a size of at least 1 line.
            if (supportingPlaceable.height > TitleItemMinContentSize.roundToPx()) {
                supportingPlaceable.placeRelative(
                    x = leadingWidth + leadingPadding,
                    y = tagsOffset,
                )
            }
        }
    }
}

private val TitleItemContainerPaddingVertical = 8.dp
private val TitleItemContainerPaddingHorizontal = 16.dp
private val TitleItemContainerHeight = 192.dp
private val TitleItemContentPadding = 4.dp
private val TitleItemTagsSpacing = 8.dp
private val TitleItemMinContentSize = 16.dp
