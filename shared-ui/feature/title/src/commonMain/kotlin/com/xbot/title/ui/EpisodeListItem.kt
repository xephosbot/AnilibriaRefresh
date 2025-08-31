package com.xbot.title.ui

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
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.modifier.scrim
import com.xbot.domain.models.Episode
import com.xbot.localization.localizedName
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.episode_title
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EpisodeListItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier
            .clickable { onClick() },
        overlineContent = {
            Text(
                text = stringResource(Res.string.episode_title)
                        + " ${formatOrdinal(episode.ordinal)} \u2022 ${episode.duration.toLocalizedString()}",
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            PosterImage(
                modifier = Modifier
                    .width(148.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                poster = episode.preview
            )
        },
        headlineContent = {
            Text(
                text = episode.localizedName().orEmpty(),
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