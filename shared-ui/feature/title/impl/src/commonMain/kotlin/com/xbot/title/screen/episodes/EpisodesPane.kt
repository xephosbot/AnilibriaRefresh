package com.xbot.title.screen.episodes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.components.ConnectedButtonGroupDefaults
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.SectionDefaults
import com.xbot.designsystem.components.SingleChoiceConnectedButtonGroup
import com.xbot.designsystem.components.section
import com.xbot.designsystem.utils.AnilibertyPreview
import com.xbot.designsystem.utils.only
import com.xbot.domain.fixtures.EpisodeFixtures
import com.xbot.domain.models.Episode
import com.xbot.domain.models.EpisodeProgress
import com.xbot.resources.Res
import com.xbot.resources.release_details_sort_newest
import com.xbot.resources.release_details_sort_oldest
import com.xbot.resources.release_details_tab_episodes
import com.xbot.title.EpisodesSort
import com.xbot.title.component.PaneSectionScope
import com.xbot.title.component.rememberPaneSectionScope
import com.xbot.title.component.sectionScroll
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
context(scope: PaneSectionScope)
internal fun EpisodesPane(
    episodes: List<Episode>,
    currentProgress: EpisodeProgress?,
    sort: EpisodesSort,
    onSortChange: (EpisodesSort) -> Unit,
    onEpisodeClick: (Episode) -> Unit,
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
        item(key = EPISODES_HEADER_KEY, contentType = EPISODES_HEADER_KEY) {
            EpisodesHeader(
                modifier = Modifier.padding(horizontalInsets),
                sort = sort,
                onSortChange = onSortChange
            )
        }
        itemsIndexed(
            items = episodes,
            key = { _, episode -> episode.id },
            contentType = { _, _ -> EPISODE_CONTENT_TYPE }
        ) { index, episode ->
            EpisodeListItem(
                modifier = Modifier.section(
                    index = index,
                    itemsCount = episodes.size,
                    sectionSpacing = SectionDefaults.spacing(contentPadding = horizontalInsets)
                ),
                episode = episode,
                selected = episode.id == currentProgress?.episodeId,
                onClick = { onEpisodeClick(episode) }
            )
        }
        item(key = BOTTOM_SPACER_KEY, contentType = BOTTOM_SPACER_KEY) {
            Spacer(Modifier.height(16.dp))
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

@AnilibertyPreview
@Composable
private fun EpisodesPanePreview() {
    with(rememberPaneSectionScope()) {
        EpisodesPane(
            episodes = EpisodeFixtures.list(count = 8),
            currentProgress = null,
            sort = EpisodesSort.OldestFirst,
            onSortChange = {},
            onEpisodeClick = {}
        )
    }
}

private const val TOP_SPACER_KEY = "top_spacer"
private const val BOTTOM_SPACER_KEY = "bottom_spacer"
private const val EPISODES_HEADER_KEY = "episodes-header"
private const val EPISODE_CONTENT_TYPE = "episode"
