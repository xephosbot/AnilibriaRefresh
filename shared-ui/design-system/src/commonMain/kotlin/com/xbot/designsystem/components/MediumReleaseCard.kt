package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.fadedEdge
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Release
import com.xbot.fixtures.data.episodeMocks
import com.xbot.fixtures.data.releaseMocks
import com.xbot.localization.localizedName

@Composable
fun MediumReleaseCard(
    release: Release?,
    onClick: (Release) -> Unit,
    modifier: Modifier = Modifier,
    shape: ExpressiveShape = ExpressiveMediumReleaseCardDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val dragged by interactionSource.collectIsDraggedAsState()

    val shape = shape.shapeForInteraction(
        pressed = pressed,
        selected = false,
        focused = focused,
        hovered = hovered,
        dragged = dragged
    )

    Crossfade(
        modifier = Modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            ) {
                release?.let { onClick(it) }
            },
        targetState = release,
        label = "ReleaseCardItem Crossfade to ${if (release == null) "Loading" else "Loaded Release"}",
    ) { state ->
        when (state) {
            null -> MediumReleaseCardPlaceholder(modifier, content)
            else -> MediumReleaseCardContent(state, modifier, content)
        }
    }
}

@Composable
private fun MediumReleaseCardContent(
    release: Release,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)?
) {
    MediumReleaseCardLayout(
        modifier = modifier,
        poster = {
            PosterImage(
                poster = release.poster,
                modifier = Modifier.fillMaxSize()
            )
        },
        title = {
            ReleaseMetaText(release = release)
            TextAutoSize(
                modifier = Modifier.fillMaxWidth(),
                text = release.localizedName(),
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    minFontSize = MaterialTheme.typography.headlineSmall.fontSize,
                ),
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        lineBreak = LineBreak.Paragraph,
                        hyphens = Hyphens.Auto
                    ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        content = {
            content?.invoke(this)
        }
    )
}

@Composable
private fun MediumReleaseCardLayout(
    modifier: Modifier = Modifier,
    poster: @Composable () -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.widthIn(max = ReleaseCardWidth),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.surfaceBright),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .height(ReleaseCardPosterHeight)
                    .fadedEdge(
                        startFraction = 0.25f,
                        endFraction = 0.75f,
                    )
            ) {
                poster()
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                title()
            }
        }
        Column(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun MediumReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)?
) {
    val shimmer = LocalShimmer.current

    MediumReleaseCardLayout(
        modifier = modifier,
        poster = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .background(Color.LightGray)
                    .fadedEdge(
                        startFraction = 0f,
                        endFraction = 1f,
                    )
            )
        },
        title = {},
        content = {
            content?.invoke(this)
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveMediumReleaseCardDefaults {
    @Composable
    fun shape(): ExpressiveShape {
        return RoundedCornerExpressiveShape(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small,
            selectedShape = MaterialTheme.shapes.small,
            focusedShape = MaterialTheme.shapes.large,
            hoveredShape = MaterialTheme.shapes.large,
            draggedShape = MaterialTheme.shapes.small,
            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
        )
    }
}

@Preview
@Composable
private fun MediumReleaseCardPreview() {
    AnilibriaPreview {
        MediumReleaseCard(
            release = releaseMocks[0],
            onClick = {},
        ) {
            EpisodeListItem(
                episode = episodeMocks.first(),
                onClick = {}
            )
        }
    }
}

private val ReleaseCardWidth = 280.dp
private val ReleaseCardPosterHeight = 284.dp
