package com.xbot.title.screen.details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ExpressiveEpisodeListItemDefaults
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ArrowForward
import com.xbot.designsystem.icons.CheckCircle
import com.xbot.designsystem.icons.PlayCircle
import com.xbot.designsystem.icons.Resume
import com.xbot.designsystem.icons.SwapVert
import com.xbot.designsystem.shape.rememberMorphableShape
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.CollectionType
import com.xbot.formatters.formatOrdinal
import com.xbot.formatters.localizedName
import com.xbot.formatters.stringRes
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.release_details_episode_number_hint
import com.xbot.resources.release_details_go_to_episode
import com.xbot.resources.release_details_not_watched
import com.xbot.resources.release_details_resume
import com.xbot.resources.release_details_sort_newest
import com.xbot.resources.release_details_sort_oldest
import com.xbot.resources.release_details_special
import com.xbot.resources.release_details_stopped_at
import com.xbot.resources.release_details_watched
import kotlin.time.Duration
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EpisodesPage(
    state: TitleDetailsStateV2,
    listState: LazyListState,
    headerSpacerHeight: Dp,
    bottomPadding: Dp,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    val episodes = state.sortedEpisodes
    val progress = state.currentProgress
    val scope = rememberCoroutineScope()
    val scrollToEpisode: (Int) -> Unit = { index ->
        if (index >= 0) {
            scope.launch { listState.scrollToItem(index + EPISODE_LIST_OFFSET) }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        item(key = HEADER_SPACER_KEY, contentType = HEADER_SPACER_KEY) {
            Spacer(Modifier.height(headerSpacerHeight))
        }
        item(key = EPISODES_TOOLBAR_KEY, contentType = EPISODES_TOOLBAR_KEY) {
            EpisodesToolbar(
                episodes = episodes,
                listState = listState,
                hasProgress = progress != null,
                sort = state.episodesSort,
                onResumeClick = {
                    scrollToEpisode(episodes.indexOfFirst { it.id == progress?.episodeId })
                },
                onSortClick = { onAction(TitleDetailsActionV2.OnEpisodesSortToggle) },
                onJumpToIndex = scrollToEpisode
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
                progress = state.episodesProgress[episode.id],
                isCurrent = episode.id == progress?.episodeId,
                onClick = { onAction(TitleDetailsActionV2.OnEpisodeClick(episode)) }
            )
        }
    }
}

@Composable
private fun EpisodesToolbar(
    episodes: List<Episode>,
    listState: LazyListState,
    hasProgress: Boolean,
    sort: EpisodesSort,
    onResumeClick: () -> Unit,
    onSortClick: () -> Unit,
    onJumpToIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val showNavigation = episodes.size > EPISODES_RANGE_SIZE
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(top = 6.dp, bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasProgress) {
                ToolbarChip(
                    text = stringResource(Res.string.release_details_resume),
                    icon = AnilibertyIcons.Resume,
                    onClick = onResumeClick
                )
            }
            ToolbarChip(
                text = stringResource(
                    when (sort) {
                        EpisodesSort.OldestFirst -> Res.string.release_details_sort_oldest
                        EpisodesSort.NewestFirst -> Res.string.release_details_sort_newest
                    }
                ),
                icon = AnilibertyIcons.SwapVert,
                onClick = onSortClick
            )
            if (showNavigation) {
                EpisodeJumpField(
                    onSubmit = { number ->
                        onJumpToIndex(episodes.indexOfFirst { it.ordinal.toInt() == number })
                    }
                )
            }
        }
        if (showNavigation) {
            EpisodeRanges(
                episodes = episodes,
                listState = listState,
                onRangeClick = { rangeIndex -> onJumpToIndex(rangeIndex * EPISODES_RANGE_SIZE) }
            )
        }
    }
}

@Composable
private fun ToolbarChip(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = text) },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(AssistChipDefaults.IconSize),
                imageVector = icon,
                contentDescription = null
            )
        },
        shape = CircleShape,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = null
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun EpisodeJumpField(onSubmit: (Int) -> Unit, modifier: Modifier = Modifier) {
    var value by rememberSaveable { mutableStateOf("") }
    val submit = { value.toIntOrNull()?.let(onSubmit) }
    Surface(
        modifier = modifier.height(AssistChipDefaults.Height),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.width(72.dp)) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.release_details_episode_number_hint),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = { input -> value = input.filter(Char::isDigit).take(5) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = { submit() })
                )
            }
            FilledIconButton(
                modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize()),
                onClick = { submit() },
                enabled = value.isNotEmpty()
            ) {
                Icon(
                    modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize),
                    imageVector = AnilibertyIcons.ArrowForward,
                    contentDescription = stringResource(Res.string.release_details_go_to_episode)
                )
            }
        }
    }
}

@Composable
private fun EpisodeRanges(
    episodes: List<Episode>,
    listState: LazyListState,
    onRangeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val rangesCount = (episodes.size + EPISODES_RANGE_SIZE - 1) / EPISODES_RANGE_SIZE
    val activeRange by remember(listState) {
        derivedStateOf {
            (
                (listState.firstVisibleItemIndex - EPISODE_LIST_OFFSET).coerceAtLeast(0) /
                    EPISODES_RANGE_SIZE
                )
                .coerceAtMost(rangesCount - 1)
        }
    }
    val rangesState = rememberLazyListState()
    LaunchedEffect(activeRange) {
        rangesState.animateScrollToItem(activeRange)
    }
    LazyRow(
        modifier = modifier.padding(top = 8.dp),
        state = rangesState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(count = rangesCount) { rangeIndex ->
            val first = episodes[rangeIndex * EPISODES_RANGE_SIZE]
            val last = episodes[minOf((rangeIndex + 1) * EPISODES_RANGE_SIZE, episodes.size) - 1]
            FilterChip(
                selected = rangeIndex == activeRange,
                onClick = { onRangeClick(rangeIndex) },
                label = {
                    Text(
                        text = "${first.ordinal.formatOrdinal()}–${last.ordinal.formatOrdinal()}"
                    )
                },
                shape = CircleShape
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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

private const val EPISODES_TOOLBAR_KEY = "episodes-toolbar"
private const val EPISODE_CONTENT_TYPE = "episode"
private const val EPISODE_LIST_OFFSET = 2
private const val EPISODES_RANGE_SIZE = 100
private val EpisodeItemHeight = 92.dp
private val EpisodePreviewWidth = 128.dp
