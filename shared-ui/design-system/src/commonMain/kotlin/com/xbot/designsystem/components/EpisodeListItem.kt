package com.xbot.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.shimmer
import com.xbot.designsystem.modifier.LocalShimmer
import com.xbot.designsystem.modifier.scrim
import com.xbot.designsystem.theme.ExpressiveShape
import com.xbot.designsystem.theme.RoundedCornerExpressiveShape
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.domain.models.Episode
import com.xbot.fixtures.data.episodeMocks
import com.xbot.localization.localizedName
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.episode_title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
fun EpisodeListItem(
    episode: Episode?,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    colors: EpisodeListItemColors = ExpressiveEpisodeListItemDefaults.colors(),
    shape: ExpressiveShape = ExpressiveEpisodeListItemDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val dragged by interactionSource.collectIsDraggedAsState()

    val shape = shape.shapeForInteraction(
        pressed = pressed,
        selected = selected,
        focused = focused,
        hovered = hovered,
        dragged = dragged
    )

    val containerColor by animateColorAsState(
        targetValue = if (selected) colors.selectedContainerColor else colors.containerColor,
        label = "EpisodeListItemContainerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) colors.selectedContentColor else colors.contentColor,
        label = "EpisodeListItemContentColor"
    )

    Surface(
        modifier = modifier,
        onClick = onClick,
        enabled = episode != null,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
    ) {
        Crossfade(
            targetState = episode,
            label = "EpisodeListItem Crossfade to ${if (episode == null) "Loading" else "Loaded Episode"}"
        ) { state ->
            if (state != null) {
                EpisodeListItemContent(state)
            } else {
                LoadingEpisodeListItem()
            }
        }
    }
}

@Composable
private fun EpisodeListItemContent(
    episode: Episode,
    modifier: Modifier = Modifier
) {
    ListItemLayout(
        modifier = modifier,
        content = {
            Text(
                text = stringResource(Res.string.episode_title) + " ${formatOrdinal(episode.ordinal)}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.weight(1f),
                text = episode.localizedName().orEmpty(),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            episode.updatedAt?.let { date ->
                Text(
                    text = date.toLocalizedString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        leadingContent = {
            Box {
                PosterImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(16f / 11f)
                        .scrim(edgeHeightRatio = 1f, opacity = 0.5f),
                    poster = episode.preview
                )
                episode.duration?.let { duration ->
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                            text = duration.toLocalizedString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                    )
                }
            }
        },
    )
}

@Composable
private fun LoadingEpisodeListItem() {
    val shimmer = LocalShimmer.current
    ListItemLayout(
        modifier = Modifier.shimmer(shimmer),
        leadingContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(16f / 11f)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.LightGray)
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(10.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(10.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(Color.LightGray)
            )
        }
    )
}

@Composable
internal fun ListItemLayout(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .heightIn(
                min = ListItemContainerHeight,
                max = ListItemContainerHeight
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        leadingContent?.invoke()
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f)
        ) {
            content()
        }
        trailingContent?.invoke()
        Spacer(Modifier.width(4.dp))
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ExpressiveEpisodeListItemDefaults {
    @Composable
    fun shape(): ExpressiveShape {
        return RoundedCornerExpressiveShape(
            shape = RoundedCornerShape(0.dp),
            pressedShape = MaterialTheme.shapes.large,
            selectedShape = MaterialTheme.shapes.large,
            focusedShape = MaterialTheme.shapes.large,
            hoveredShape = MaterialTheme.shapes.medium,
            draggedShape = MaterialTheme.shapes.large,
            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
        )
    }

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
        contentColor: Color = contentColorFor(containerColor),
        selectedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        selectedContentColor: Color = contentColorFor(selectedContainerColor)
    ): EpisodeListItemColors = EpisodeListItemColors(
        containerColor = containerColor,
        contentColor = contentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor
    )
}

@Immutable
data class EpisodeListItemColors(
    val containerColor: Color,
    val contentColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color
)

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun EpisodeListItemPreview() {
    AnilibriaPreview {
        EpisodeListItem(
            episode = episodeMocks.first(),
            selected = false,
            onClick = {

            }
        )
    }
}

@Preview
@Composable
private fun EpisodeListItemLoadingPreview() {
    AnilibriaPreview {
        EpisodeListItem(
            episode = null,
            onClick = {}
        )
    }
}

private fun formatOrdinal(ordinal: Float): String {
    return if (ordinal == ordinal.toInt().toFloat()) {
        ordinal.toInt().toString()
    } else {
        ordinal.toString()
    }
}

private val ListItemContainerHeight = 88.dp
