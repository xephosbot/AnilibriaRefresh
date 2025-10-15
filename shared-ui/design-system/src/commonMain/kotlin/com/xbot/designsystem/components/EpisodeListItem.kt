package com.xbot.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.modifier.scrim
import com.xbot.designsystem.utils.PreviewContainer
import com.xbot.domain.models.Episode
import com.xbot.localization.localizedName
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.episode_title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun EpisodeListItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ListItemLayout(
        modifier = modifier.clickable { onClick() },
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
            Text(
                text = episode.updatedAt.toLocalizedString(),
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
internal fun ListItemLayout(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceBright)
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

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun EpisodeListItemPreview() {
    PreviewContainer {
        EpisodeListItem(
            episode = Episode(
                id = "1",
                name = "Episode 1",
                ordinal = 1.0f,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            ),
            onClick = {

            }
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