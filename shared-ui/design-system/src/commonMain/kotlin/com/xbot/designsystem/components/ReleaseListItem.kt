package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Release
import com.xbot.fixtures.data.releaseMocks
import com.xbot.localization.localizedName

@Composable
fun ReleaseListItem(
    release: Release?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    shape: ExpressiveShape = ExpressiveReleaseListItemDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    onClick: (Release) -> Unit = {},
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Surface(
        modifier = modifier,
        shape = shape.shapeForInteraction(pressed, false),
        color = containerColor,
        contentColor = contentColor
    ) {
        Crossfade(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ) {
                    release?.let { onClick(it) }
                },
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
                text = release.localizedName(),
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
            ReleaseMetaText(release = release)
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
            .shimmer(shimmer),
        headlineContent = {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.LightGray),
            )
        },
        supportingContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.extraSmall)
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
                            .clip(MaterialTheme.shapes.extraSmall)
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

        val headlineOffset = verticalPadding
        val tagsOffset = headlineOffset + headlinePlaceable.height

        val supportingHeight = (headlineHeight - headlinePlaceable.height - spacing).coerceAtLeast(0)
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
                y = headlineOffset,
            )
            tagsPlaceable.placeRelative(
                x = leadingWidth + leadingPadding,
                y = tagsOffset,
            )
            supportingPlaceable.placeRelative(
                x = leadingWidth + leadingPadding,
                y = tagsOffset + tagsPlaceable.height + spacing,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveReleaseListItemDefaults {
    private var _shape: ExpressiveShape? = null

    @Composable
    fun shape(): ExpressiveShape {
        return _shape ?: RoundedCornerExpressiveShape(
            shape = RectangleShape,
            pressedShape = MaterialTheme.shapes.large,
            selectedShape = MaterialTheme.shapes.large,
            animationSpec = spring()
        ).also { _shape = it }
    }
}

private val ReleaseItemContainerPaddingVertical = 12.dp
private val ReleaseItemContainerPaddingHorizontal = 16.dp
private val ReleaseItemContainerHeight = 160.dp
private val ReleaseItemContentSpacingVertical = 4.dp
private val ReleaseItemTagsSpacing = 8.dp
private const val SubtitleAlpha = 0.6f
private const val DescriptionAlpha = 0.8f

@Preview
@Composable
private fun ReleaseListItemPreview() {
    AnilibriaPreview {
        ReleaseListItem(
            release = releaseMocks.first()
        )
    }
}

@Preview
@Composable
private fun ReleaseListItemLoadingPreview() {
    AnilibriaPreview {
        ReleaseListItem(
            release = null
        )
    }
}
