package com.xbot.shared.ui.feature.title.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.shared.domain.models.Episode
import com.xbot.shared.resources.Res
import com.xbot.shared.resources.episode_title
import com.xbot.shared.ui.designsystem.components.PosterImage
import com.xbot.shared.ui.designsystem.modifier.scrim
import com.xbot.shared.ui.localization.localizedName
import com.xbot.shared.ui.localization.toLocalizedString
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

@Composable
internal fun EpisodeListItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val title = remember {
        runBlocking { buildEpisodeTitle(episode) }
    }
    ListItem(
        modifier = modifier
            .clickable { onClick() },
        leadingContent = {
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.BottomEnd
            ) {
                PosterImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .scrim(0.25f, 0.7f),
                    poster = episode.preview
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = episode.duration.toLocalizedString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = episode.updatedAt.toLocalizedString(),
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

private suspend fun buildEpisodeTitle(episode: Episode): String {
    return buildString {
        append(getString(Res.string.episode_title))
        append(" ")
        append(formatOrdinal(episode.ordinal))
        episode.localizedName()?.let { name ->
            append(" \u2022 ")
            append(name)
        }
    }
}

private fun formatOrdinal(ordinal: Float): String {
    return if (ordinal == ordinal.toInt().toFloat()) {
        ordinal.toInt().toString()
    } else {
        ordinal.toString()
    }
}