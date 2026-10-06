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
import com.xbot.common.getOrNull
import com.xbot.designsystem.components.ExpressiveEpisodeListItemDefaults
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.ChevronRight
import com.xbot.designsystem.icons.ExpandMore
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.SmartDisplay
import com.xbot.designsystem.icons.Timer
import com.xbot.designsystem.icons.Update
import com.xbot.designsystem.shape.rememberMorphableShape
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetails
import com.xbot.domain.models.enums.CollectionType
import com.xbot.formatters.stringRes
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.episode_abbreviation
import com.xbot.resources.label_genres
import com.xbot.resources.label_members
import com.xbot.resources.release_details_announced
import com.xbot.resources.release_details_details
import com.xbot.resources.release_details_external_player
import com.xbot.resources.release_details_franchise
import com.xbot.resources.release_details_franchise_all
import com.xbot.resources.release_details_last_update
import com.xbot.resources.release_details_player_not_working
import com.xbot.resources.release_details_read_more
import com.xbot.resources.release_details_show_less
import com.xbot.resources.release_details_synopsis
import com.xbot.resources.release_details_total_duration
import com.xbot.resources.release_details_total_duration_value
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AboutPage(
    state: TitleDetailsStateV2,
    details: ReleaseDetails,
    listState: LazyListState,
    headerSpacerHeight: Dp,
    bottomPadding: Dp,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        item(key = HEADER_SPACER_KEY, contentType = HEADER_SPACER_KEY) {
            Spacer(Modifier.height(headerSpacerHeight))
        }
        details.release.description?.takeIf { it.isNotBlank() }?.let { description ->
            item(key = "synopsis") {
                AboutSection(title = stringResource(Res.string.release_details_synopsis)) {
                    Synopsis(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = description
                    )
                }
            }
        }
        val franchiseReleases = state.franchiseReleases.getOrNull().orEmpty()
        if (franchiseReleases.isNotEmpty()) {
            item(key = FRANCHISE_KEY) {
                AboutSection(
                    title = stringResource(Res.string.release_details_franchise),
                    action = {
                        TextButton(
                            onClick = { onAction(TitleDetailsActionV2.OnFranchiseAllClick) }
                        ) {
                            Text(text = stringResource(Res.string.release_details_franchise_all))
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                imageVector = AnilibertyIcons.ChevronRight,
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    FranchiseCarousel(
                        releases = franchiseReleases,
                        onReleaseClick = { release ->
                            onAction(TitleDetailsActionV2.OnFranchiseReleaseClick(release))
                        }
                    )
                }
            }
        }
        if (details.genres.isNotEmpty()) {
            item(key = "genres") {
                AboutSection(title = stringResource(Res.string.label_genres)) {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        details.genres.forEach { genre ->
                            GenreChip(
                                genre = genre,
                                onClick = { onAction(TitleDetailsActionV2.OnGenreClick(genre)) }
                            )
                        }
                    }
                }
            }
        }
        if (details.releaseMembers.isNotEmpty()) {
            item(key = "members") {
                AboutSection(title = stringResource(Res.string.label_members)) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = details.releaseMembers, key = { it.id }) { member ->
                            MemberItem(
                                releaseMember = member,
                                onClick = { onAction(TitleDetailsActionV2.OnMemberClick(member)) }
                            )
                        }
                    }
                }
            }
        }
        item(key = "details") {
            AboutSection(title = stringResource(Res.string.release_details_details)) {
                ReleaseDetailsList(details = details, onAction = onAction)
            }
        }
    }
}

@Composable
internal fun AboutSection(
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.padding(top = 28.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            action?.invoke()
        }
        content()
    }
}

@Composable
private fun Synopsis(text: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var overflows by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)
    val fadeColor = MaterialTheme.colorScheme.surfaceContainer

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .animateContentSize()
                .drawWithContent {
                    drawContent()
                    if (!expanded && overflows) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                0.6f to Color.Transparent,
                                1f to fadeColor
                            )
                        )
                    }
                },
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (expanded) Int.MAX_VALUE else SYNOPSIS_COLLAPSED_LINES,
            onTextLayout = { result -> if (!expanded) overflows = result.hasVisualOverflow }
        )
        if (overflows || expanded) {
            FilledTonalButton(
                modifier = Modifier.padding(top = 6.dp),
                onClick = { expanded = !expanded },
                contentPadding = ButtonDefaults.SmallContentPadding
            ) {
                Icon(
                    modifier = Modifier
                        .size(ButtonDefaults.IconSize)
                        .rotate(rotation),
                    imageVector = AnilibertyIcons.ExpandMore,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = ButtonDefaults.IconSpacing),
                    text = stringResource(
                        if (expanded) {
                            Res.string.release_details_show_less
                        } else {
                            Res.string.release_details_read_more
                        }
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FranchiseCarousel(
    releases: List<Release>,
    onReleaseClick: (Release) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { releases.size },
        preferredItemWidth = FranchiseCardWidth,
        modifier = modifier
            .fillMaxWidth()
            .height(FranchiseCardHeight),
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        val release = releases[index]
        FranchiseCard(
            modifier = Modifier
                .maskClip(MaterialTheme.shapes.extraLarge)
                .clickable { onReleaseClick(release) },
            release = release
        )
    }
}

@Composable
private fun FranchiseCard(release: Release, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        PosterImage(
            modifier = Modifier.fillMaxSize(),
            poster = release.poster
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.35f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.78f)
                    )
                )
        )
        val status = if (release.isInProduction) {
            stringResource(Res.string.release_details_announced)
        } else {
            null
        }
        status?.let { text ->
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = release.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = listOfNotNull(
                    release.type?.let { stringResource(it.stringRes) },
                    release.year.toString(),
                    release.episodesCount?.let {
                        stringResource(Res.string.episode_abbreviation, it.toString())
                    }
                ).joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun GenreChip(genre: Genre, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier
                .height(44.dp)
                .padding(start = 4.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PosterImage(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                poster = genre.image
            )
            Text(
                text = genre.name,
                style = MaterialTheme.typography.labelLarge
            )
            genre.releasesCount?.let { count ->
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReleaseDetailsList(
    details: ReleaseDetails,
    onAction: (TitleDetailsActionV2) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalDuration = details.release.totalDurationMinutes?.let { minutes ->
        DetailsRow(
            icon = AnilibertyIcons.Timer,
            label = stringResource(Res.string.release_details_total_duration),
            value = stringResource(
                Res.string.release_details_total_duration_value,
                minutes.minutes.toLocalizedString()
            )
        )
    }
    val lastUpdate = details.freshAt?.let { freshAt ->
        DetailsRow(
            icon = AnilibertyIcons.Update,
            label = stringResource(Res.string.release_details_last_update),
            value = freshAt.toLocalizedString()
        )
    }
    val externalPlayer = if (details.externalPlayerUrl != null) {
        DetailsRow(
            icon = AnilibertyIcons.SmartDisplay,
            label = stringResource(Res.string.release_details_player_not_working),
            value = stringResource(Res.string.release_details_external_player),
            action = TitleDetailsActionV2.OnExternalPlayerClick
        )
    } else {
        null
    }
    val rows = listOfNotNull(totalDuration, lastUpdate, externalPlayer)
    Column(modifier = modifier) {
        rows.forEachIndexed { index, row ->
            PreferenceItem(
                modifier = Modifier.section(index, rows.size),
                headlineContent = { Text(text = row.value) },
                supportingContent = { Text(text = row.label) },
                leadingContent = {
                    Icon(
                        imageVector = row.icon,
                        contentDescription = null
                    )
                },
                trailingContent = row.action?.let {
                    {
                        Icon(
                            imageVector = AnilibertyIcons.OpenInNew,
                            contentDescription = null
                        )
                    }
                },
                onClick = row.action?.let { action -> { onAction(action) } }
            )
        }
    }
}

private data class DetailsRow(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val action: TitleDetailsActionV2? = null
)

private const val FRANCHISE_KEY = "franchise"
private const val SYNOPSIS_COLLAPSED_LINES = 4
private val FranchiseCardWidth = 172.dp
private val FranchiseCardHeight = 244.dp
