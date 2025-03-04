package com.xbot.title.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.common.localization.localizedName
import com.xbot.common.localization.toLocalizedString
import com.xbot.designsystem.components.PosterImage
import com.xbot.domain.models.Episode
import com.xbot.title.R

@Composable
internal fun EpisodeListItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier
            .clickable { onClick() },
        leadingContent = {
            PosterImage(
                modifier = Modifier
                    .width(124.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                poster = episode.preview
            )
        },
        headlineContent = {
            Text(
                text = buildEpisodeTitle(episode, LocalContext.current),
                style = MaterialTheme.typography.titleMedium,
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

private fun buildEpisodeTitle(episode: Episode, context: Context): String {
    return buildString {
        append(context.getString(R.string.episode_title))
        append(" ")
        append(formatOrdinal(episode.ordinal))
        episode.localizedName()?.let { name ->
            append(": ")
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