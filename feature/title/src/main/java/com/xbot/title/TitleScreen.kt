package com.xbot.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.AssistChip
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.ExpandableText
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.FeedItemScope
import com.xbot.designsystem.components.FeedScope
import com.xbot.designsystem.components.ModalScrollableBottomSheet
import com.xbot.designsystem.components.PosterImage
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.ReleaseLargeCard
import com.xbot.designsystem.components.SuggestionButton
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.pinnedScrollBehaviorFixed
import com.xbot.designsystem.components.row
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Episode
import com.xbot.domain.models.ReleaseDetail
import com.xbot.title.ui.EpisodeListItem
import com.xbot.title.ui.MemberItem
import com.xbot.title.ui.PlayButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onPlayClick = onPlayClick,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onAction: (TitleScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehaviorFixed()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Crossfade(
            targetState = state,
            label = ""
        ) { targetState ->
            when (targetState) {
                is TitleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is TitleScreenState.Success -> {
                    TitleDetails(
                        details = targetState.title,
                        contentPadding = innerPadding,
                        onPlayClick = onPlayClick,
                        onReleaseClick = onReleaseClick,
                        onAddToFavoritesClick = {},
                        onAlreadyWatchedClick = {},
                        onShareClick = {},
                        onTelegramClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleDetails(
    modifier: Modifier = Modifier,
    details: ReleaseDetail,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    var showEpisodesList by rememberSaveable { mutableStateOf(false) }

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            columns = GridCells.Adaptive(350.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            row {
                ReleaseLargeCard(release = details.release)
            }

            swapItems(
                content1 = {
                    SuggestionRow(
                        onAddToFavoritesClick = onAddToFavoritesClick,
                        onAlreadyWatchedClick = onAlreadyWatchedClick,
                        onShareClick = onShareClick,
                        onTelegramClick = onTelegramClick
                    )
                },
                content2 = {
                    PlayButton(
                        modifier = Modifier.fillMaxWidth(),
                        onLeadingClick = { onPlayClick(details.release.id, 0) },
                        onTrailingClick = { showEpisodesList = true },
                        trailingEnabled = details.episodes.isNotEmpty()
                    )
                }
            )

            if (details.release.description.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(R.string.label_description)) }
                )
                row {
                    ExpandableText(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = details.release.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            header(
                title = { Text(text = stringResource(R.string.label_genres)) }
            )
            row {
                ChipGroup(
                    items = details.genres
                ) { genre ->
                    AssistChip(
                        onClick = {},
                        label = { Text(text = genre.name) }
                    )
                }
            }

            if (details.members.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(R.string.label_members)) }
                )
                horizontalItems(
                    items = details.members,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { member ->
                    MemberItem(
                        member = member,
                        onClick = { /*TODO*/ }
                    )
                }
            }

            if (details.relatedReleases.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(R.string.label_related_releases)) }
                )
                horizontalItems(
                    items = details.relatedReleases,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) { release ->
                    ReleaseCardItem(
                        release = release,
                        onClick = { onReleaseClick(it.id) }
                    )
                }
            }

            row { Spacer(Modifier.height(16.dp)) }
        }
    }

    val episodesListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (showEpisodesList) {
        ModalScrollableBottomSheet(
            onDismissRequest = {
                scope.launch {
                    episodesListState.scrollToItem(0)
                }
                showEpisodesList = false
            },
            scrollableState = episodesListState
        ) {
            EpisodesList(
                episodes = details.episodes.reversed(),
                state = episodesListState
            ) { ordinal ->
                onPlayClick(details.release.id, ordinal)
            }
        }
    }
}

@Composable
private fun EpisodesList(
    episodes: List<Episode>,
    modifier: Modifier = Modifier,
    state: LazyListState,
    onEpisodeClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            state = state,
            reverseLayout = false
        ) {
            itemsIndexed(episodes) { index, episode ->
                EpisodeListItem(episode) {
                    onEpisodeClick(index)
                }
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    modifier: Modifier = Modifier,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SuggestionButton(
            modifier = Modifier.weight(1f),
            onClick = onAddToFavoritesClick,
            text = stringResource(R.string.button_add_to_favorites),
            icon = AnilibriaIcons.Filled.Star,
        )
        SuggestionButton(
            modifier = Modifier.weight(1f),
            onClick = onAlreadyWatchedClick,
            text = stringResource(R.string.button_watched_it),
            icon = AnilibriaIcons.Outlined.CheckList,
        )
        SuggestionButton(
            modifier = Modifier.weight(1f),
            onClick = onShareClick,
            text = stringResource(R.string.button_share),
            icon = AnilibriaIcons.Filled.Share,
        )
        SuggestionButton(
            modifier = Modifier.weight(1f),
            onClick = onTelegramClick,
            text = stringResource(R.string.button_telegram),
            icon = AnilibriaIcons.Filled.TelegramLogo,
        )
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            ReleaseLargeCard(null)
        }
    }
}

private fun FeedScope.swapItems(
    span: (LazyGridItemSpanScope.() -> GridItemSpan)? = { GridItemSpan(1) },
    content1: @Composable FeedItemScope.() -> Unit,
    content2: @Composable FeedItemScope.() -> Unit,
) {
    item(span = span) {
        Box(
            modifier = Modifier
                .feedItemSpacing(0)
                .padding(bottom = if (maxLineSpan == 1) 16.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (maxLineSpan == 1) {
                content1()
            } else {
                content2()
            }
        }
    }
    item(span = span) {
        Box(
            modifier = Modifier
                .feedItemSpacing(1),
            contentAlignment = Alignment.Center
        ) {
            if (maxLineSpan == 1) {
                content2()
            } else {
                content1()
            }
        }
    }
}
