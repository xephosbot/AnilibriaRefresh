package com.xbot.title.screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ExpandableText
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.PreferenceItem
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibertyIcons
import com.xbot.designsystem.icons.OpenInNew
import com.xbot.designsystem.icons.SmartDisplay
import com.xbot.designsystem.icons.Timer
import com.xbot.designsystem.icons.Update
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.fixtures.createReleaseDetails
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseMember
import com.xbot.formatters.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.label_genres
import com.xbot.resources.label_members
import com.xbot.resources.release_details_details
import com.xbot.resources.release_details_external_player
import com.xbot.resources.release_details_franchise
import com.xbot.resources.release_details_last_update
import com.xbot.resources.release_details_player_not_working
import com.xbot.resources.release_details_read_more
import com.xbot.resources.release_details_show_less
import com.xbot.resources.release_details_synopsis
import com.xbot.resources.release_details_total_duration
import com.xbot.resources.release_details_total_duration_value
import com.xbot.title.component.PaneSectionScope
import com.xbot.title.component.rememberPaneSectionScope
import com.xbot.title.component.sectionScroll
import com.xbot.title.component.totalDurationMinutes
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
context(scope: PaneSectionScope)
internal fun AboutPane(
    description: String?,
    franchiseReleases: List<Release>,
    genres: List<Genre>,
    members: List<ReleaseMember>,
    totalDurationMinutes: Int?,
    lastUpdate: LocalDateTime?,
    hasExternalPlayer: Boolean,
    onFranchiseAllClick: () -> Unit,
    onFranchiseReleaseClick: (Release) -> Unit,
    onGenreClick: (Genre) -> Unit,
    onMemberClick: (ReleaseMember) -> Unit,
    onExternalPlayerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val horizontalInsets = scope.contentPadding only WindowInsetsSides.Horizontal

    LazyColumn(
        modifier = modifier.sectionScroll(listState),
        state = listState,
        contentPadding = scope.contentPadding only WindowInsetsSides.Bottom
    ) {
        item(key = TOP_SPACER_KEY, contentType = TOP_SPACER_KEY) {
            Spacer(Modifier.height(scope.contentPadding.calculateTopPadding()))
        }
        description?.takeIf { it.isNotBlank() }?.let { description ->
            item(key = "synopsis_header") {
                Header(modifier = Modifier.padding(horizontalInsets), title = {
                    Text(text = stringResource(Res.string.release_details_synopsis))
                })
            }
            item(key = "synopsis") {
                ExpandableText(
                    modifier = Modifier
                        .padding(horizontalInsets)
                        .padding(horizontal = 16.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    collapsedMaxLine = SYNOPSIS_COLLAPSED_LINES,
                    showMoreText = "… " + stringResource(Res.string.release_details_read_more),
                    showLessText = " " + stringResource(Res.string.release_details_show_less)
                )
            }
        }
        if (franchiseReleases.isNotEmpty()) {
            item(key = "franchise_header") {
                Header(
                    modifier = Modifier.padding(horizontalInsets),
                    title = { Text(text = stringResource(Res.string.release_details_franchise)) },
                    onClick = onFranchiseAllClick
                )
            }
            item(key = FRANCHISE_KEY) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp) + horizontalInsets,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = franchiseReleases, key = { it.id }) { release ->
                        SmallReleaseCard(
                            release = release,
                            onClick = onFranchiseReleaseClick
                        )
                    }
                }
            }
        }
        if (genres.isNotEmpty()) {
            item(key = "genres_header") {
                Header(modifier = Modifier.padding(horizontalInsets), title = {
                    Text(text = stringResource(Res.string.label_genres))
                })
            }
            item(key = "genres") {
                FlowRow(
                    modifier = Modifier
                        .padding(horizontalInsets)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEach { genre ->
                        GenreChip(
                            genre = genre,
                            onClick = { onGenreClick(genre) }
                        )
                    }
                }
            }
        }
        if (members.isNotEmpty()) {
            item(key = "members_header") {
                Header(modifier = Modifier.padding(horizontalInsets), title = {
                    Text(text = stringResource(Res.string.label_members))
                })
            }
            item(key = "members") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp) + horizontalInsets,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = members, key = { it.id }) { member ->
                        MemberItem(
                            releaseMember = member,
                            onClick = { onMemberClick(member) }
                        )
                    }
                }
            }
        }
        item(key = "details_header") {
            Header(modifier = Modifier.padding(horizontalInsets), title = {
                Text(text = stringResource(Res.string.release_details_details))
            })
        }
        item(key = "details") {
            ReleaseDetailsList(
                modifier = Modifier.padding(horizontalInsets),
                totalDurationMinutes = totalDurationMinutes,
                lastUpdate = lastUpdate,
                onExternalPlayerClick = onExternalPlayerClick.takeIf { hasExternalPlayer }
            )
        }
        item(key = BOTTOM_SPACER_KEY, contentType = BOTTOM_SPACER_KEY) {
            Spacer(Modifier.height(16.dp))
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
    totalDurationMinutes: Int?,
    lastUpdate: LocalDateTime?,
    onExternalPlayerClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val totalDuration = totalDurationMinutes?.let { minutes ->
        DetailsRow(
            icon = AnilibertyIcons.Timer,
            label = stringResource(Res.string.release_details_total_duration),
            value = stringResource(
                Res.string.release_details_total_duration_value,
                minutes.minutes.toLocalizedString()
            )
        )
    }
    val lastUpdateRow = lastUpdate?.let { freshAt ->
        DetailsRow(
            icon = AnilibertyIcons.Update,
            label = stringResource(Res.string.release_details_last_update),
            value = freshAt.toLocalizedString()
        )
    }
    val externalPlayer = onExternalPlayerClick?.let { onClick ->
        DetailsRow(
            icon = AnilibertyIcons.SmartDisplay,
            label = stringResource(Res.string.release_details_player_not_working),
            value = stringResource(Res.string.release_details_external_player),
            onClick = onClick
        )
    }
    val rows = listOfNotNull(totalDuration, lastUpdateRow, externalPlayer)
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
                trailingContent = row.onClick?.let {
                    {
                        Icon(
                            imageVector = AnilibertyIcons.OpenInNew,
                            contentDescription = null
                        )
                    }
                },
                onClick = row.onClick
            )
        }
    }
}

private data class DetailsRow(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val onClick: (() -> Unit)? = null
)

@AnilibertyPreview
@Composable
private fun AboutPanePreview() {
    val details = createReleaseDetails()
    with(rememberPaneSectionScope()) {
        AboutPane(
            description = details.release.description,
            franchiseReleases = ReleaseFixtures.list(count = 3),
            genres = details.genres,
            members = details.releaseMembers,
            totalDurationMinutes = details.release.totalDurationMinutes,
            lastUpdate = details.freshAt,
            hasExternalPlayer = details.externalPlayerUrl != null,
            onFranchiseAllClick = {},
            onFranchiseReleaseClick = {},
            onGenreClick = {},
            onMemberClick = {},
            onExternalPlayerClick = {}
        )
    }
}

private const val TOP_SPACER_KEY = "top_spacer"
private const val BOTTOM_SPACER_KEY = "bottom_spacer"
private const val FRANCHISE_KEY = "franchise"
private const val SYNOPSIS_COLLAPSED_LINES = 4
