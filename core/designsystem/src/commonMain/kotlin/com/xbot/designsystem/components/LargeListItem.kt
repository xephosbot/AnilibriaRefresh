package com.xbot.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LargeListItem(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    leadingContent: @Composable () -> Unit,
    tags: @Composable RowScope.() -> Unit,
) {
    TitleItemLayout(
        modifier = modifier
            .height(TitleItemContainerHeight)
            .padding(
                vertical = TitleItemContainerPaddingVertical,
                horizontal = TitleItemContainerPaddingHorizontal
            ),
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        tags = tags,
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
