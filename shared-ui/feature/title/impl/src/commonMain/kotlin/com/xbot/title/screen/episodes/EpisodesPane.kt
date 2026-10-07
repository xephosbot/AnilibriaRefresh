package com.xbot.title.screen.episodes

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.ExpressiveEpisodeListItemDefaults
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.CheckCircle
import com.xbot.designsystem.icons.PlayCircle
import com.xbot.designsystem.shape.rememberMorphableShape
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.formatters.localizedName
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.release_details_not_watched
import com.xbot.resources.release_details_sort_newest
import com.xbot.resources.release_details_sort_oldest
import com.xbot.resources.release_details_special
import com.xbot.resources.release_details_stopped_at
import com.xbot.resources.release_details_tab_episodes
import com.xbot.resources.release_details_watched
import com.xbot.title.component.PaneSectionScope
import com.xbot.title.component.episodeLabel
import com.xbot.title.component.formatPlayback
import com.xbot.title.component.isSpecial
import com.xbot.title.component.rememberPaneSectionScope
import com.xbot.title.component.watchedFraction
import com.xbot.title.screen.details2.EpisodesSort
import com.xbot.title.screen.details2.TitleDetailsPreviewDataV2
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
context(scope: PaneSectionScope)
internal fun EpisodesPane(
    episodes: List<Episode>,
    episodesProgress: Map<String, EpisodeProgress>,
    currentProgress: EpisodeProgress?,
    sort: EpisodesSort,
    onSortChange: (EpisodesSort) -> Unit,
    onEpisodeClick: (Episode) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = scope.listState,
        contentPadding = PaddingValues(bottom = scope.contentPadding.calculateBottomPadding())
    ) {
        item(key = TOP_SPACER_KEY, contentType = TOP_SPACER_KEY) {
            Spacer(Modifier.height(scope.contentPadding.calculateTopPadding()))
        }
        item(key = EPISODES_HEADER_KEY, contentType = EPISODES_HEADER_KEY) {
            EpisodesHeader(
                sort = sort,
                onSortChange = onSortChange
            )
        }
        itemsIndexed(
            items = episodes,
            key = { _, episode -> episode.id },
            contentType = { _, _ -> EPISODE_CONTENT_TYPE }
        ) { index, episode ->
            EpisodeItem(
                modifier = Modifier.section(index, episodes.size),
                episode = episode,
                progress = episodesProgress[episode.id],
                isCurrent = episode.id == currentProgress?.episodeId,
                onClick = { onEpisodeClick(episode) }
            )
        }
    }
}

@Composable
private fun EpisodesHeader(
    sort: EpisodesSort,
    onSortChange: (EpisodesSort) -> Unit,
    modifier: Modifier = Modifier
) {
    Header(
        modifier = modifier,
        title = { Text(text = stringResource(Res.string.release_details_tab_episodes)) },
        content = {
            val items = EpisodesSort.entries
            SingleChoiceConnectedButtonGroup(
                items = items,
                selectedItem = sort
            ) { selected, item ->
                FilledTonalToggleButton(
                    modifier = Modifier.height(ButtonDefaults.ExtraSmallContainerHeight),
                    checked = selected,
                    onCheckedChange = { onSortChange(item) },
                    shapes = ConnectedButtonGroupDefaults.connectedButtonShapes(
                        index = items.indexOf(item),
                        count = items.size
                    ),
                    contentPadding = ButtonDefaults.ExtraSmallContentPadding
                ) {
                    Text(text = stringResource(item.labelRes))
                }
            }
        }
    )
}

private val EpisodesSort.labelRes: StringResource
    get() = when (this) {
        EpisodesSort.OldestFirst -> Res.string.release_details_sort_oldest
        EpisodesSort.NewestFirst -> Res.string.release_details_sort_newest
    }

@Composable
private fun EpisodeItem(
    episode: Episode,
    progress: EpisodeProgress?,
    isCurrent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = rememberMorphableShape(
        shapes = ExpressiveEpisodeListItemDefaults.shapes(),
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        pressed = pressed,
        selected = isCurrent
    )
    val colors = ExpressiveEpisodeListItemDefaults.colors(
        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
    )
    val label = episodeLabel(episode.ordinal)
    val name = episode.localizedName()?.takeIf { it.isNotBlank() }
    val stoppedAt = progress?.position?.takeIf { isCurrent }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = if (isCurrent) colors.selectedContainerColor else colors.containerColor,
        contentColor = if (isCurrent) colors.selectedContentColor else colors.contentColor,
        interactionSource = interactionSource
    ) {
        Row(modifier = Modifier.height(EpisodeItemHeight)) {
            EpisodePreview(episode = episode, progress = progress)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 14.dp, top = 10.dp, end = 4.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
            ) {
                if (name != null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f, fill = false),
                        text = name ?: label,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (episode.isSpecial) {
                        SpecialBadge(modifier = Modifier.padding(start = 6.dp))
                    }
                }
                val bottomLine = when {
                    stoppedAt != null -> stringResource(
                        Res.string.release_details_stopped_at,
                        stoppedAt.formatPlayback()
                    )

                    else -> episode.updatedAt?.toLocalizedString()
                }
                bottomLine?.let { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 6.dp, end = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                if (progress?.isWatched == true) {
                    Icon(
                        imageVector = AnilibertyIcons.Filled.CheckCircle,
                        contentDescription = stringResource(Res.string.release_details_watched),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = AnilibertyIcons.PlayCircle,
                        contentDescription = stringResource(Res.string.release_details_not_watched),
                        tint = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodePreview(
    episode: Episode,
    progress: EpisodeProgress?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(EpisodePreviewWidth)
            .fillMaxHeight()
    ) {
        PosterImage(
            modifier = Modifier.fillMaxSize(),
            poster = episode.preview
        )
        episode.duration?.let { duration ->
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(Color.Black.copy(alpha = 0.62f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 1.dp),
                text = duration.formatPlayback(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
        val fraction = episode.watchedFraction(progress)
        if (fraction != null && fraction > 0f && fraction < 1f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.28f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
private fun SpecialBadge(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 1.dp),
        text = stringResource(Res.string.release_details_special),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer
    )
}

@AnilibertyPreview
@Composable
private fun EpisodesPanePreview() {
    val state = TitleDetailsPreviewDataV2.ongoing
    with(rememberPaneSectionScope()) {
        EpisodesPane(
            episodes = state.sortedEpisodes,
            episodesProgress = state.episodesProgress,
            currentProgress = state.currentProgress,
            sort = state.episodesSort,
            onSortChange = {},
            onEpisodeClick = {}
        )
    }
}

private const val TOP_SPACER_KEY = "top_spacer"
private const val EPISODES_HEADER_KEY = "episodes-header"
private const val EPISODE_CONTENT_TYPE = "episode"
private val EpisodeItemHeight = 92.dp
private val EpisodePreviewWidth = 128.dp
