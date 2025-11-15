package com.xbot.title

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.ExpandableText
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.FeedItemScope
import com.xbot.designsystem.components.FeedScope
import com.xbot.designsystem.components.LabeledIconButton
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MediumSplitButton
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.TypedCrossFade
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.row
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Checklist
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.icons.PlaylistPlay
import com.xbot.designsystem.icons.Share
import com.xbot.designsystem.icons.Star
import com.xbot.designsystem.icons.TelegramLogo
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.resources.Res
import com.xbot.resources.alert_blocked_copyright
import com.xbot.resources.alert_blocked_geo
import com.xbot.resources.button_add_to_favorites
import com.xbot.resources.button_share
import com.xbot.resources.button_telegram
import com.xbot.resources.button_watch_continue
import com.xbot.resources.button_watched_it
import com.xbot.resources.label_description
import com.xbot.resources.label_genres
import com.xbot.resources.label_members
import com.xbot.resources.label_related_releases
import com.xbot.title.ui.AlertCard
import com.xbot.title.ui.NotificationCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
internal fun TitleDetailsPane(
    modifier: Modifier = Modifier,
    viewModel: TitleDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodesListClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val gridState = rememberLazyGridState()
    var selected by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    FilledIconButton(
                        onClick = onBackClick,
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    FilledIconToggleButton(
                        checked = selected,
                        onCheckedChange = { selected = it },
                        shapes = IconButtonDefaults.toggleableShapes(),
                        colors = IconButtonDefaults.filledIconToggleButtonColors(
                            checkedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            checkedContentColor = MaterialTheme.colorScheme.contentColorFor(
                                MaterialTheme.colorScheme.inverseSurface
                            ),
                        ),
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.Filled.Star,
                            contentDescription = null
                        )
                    }
                    FilledIconButton(
                        onClick = {},
                        modifier = Modifier.size(
                            IconButtonDefaults.smallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Narrow
                            )
                        ),
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = AnilibriaIcons.MoreVert,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f)
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        TypedCrossFade(
            targetState = state,
        ) { targetState ->
            when (targetState) {
                is TitleDetailsScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is TitleDetailsScreenState.Success -> {
                    TitleDetails(
                        gridState = gridState,
                        details = targetState.title,
                        contentPadding = innerPadding,
                        onPlayClick = onPlayClick,
                        onReleaseClick = onReleaseClick,
                        onEpisodesListClick = onEpisodesListClick,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TitleDetails(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    details: ReleaseDetail,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
    onEpisodesListClick: () -> Unit,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            state = gridState,
            columns = GridCells.Adaptive(400.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
            row {
                LargeReleaseCard(
                    modifier = Modifier.verticalParallax(gridState),
                    release = details.release
                )
            }

            if (details.availabilityStatus == AvailabilityStatus.Available) {
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
                        MediumSplitButton(
                            onLeadingClick = {
                                onPlayClick(details.release.id, 0)
                            },
                            onTrailingClick = {
                                onEpisodesListClick()
                            },
                            leadingContent = {
                                Icon(
                                    modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                                    imageVector = AnilibriaIcons.Filled.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
                                Text(
                                    text = stringResource(Res.string.button_watch_continue),
                                    maxLines = 1
                                )
                            },
                            trailingContent = {
                                Icon(
                                    modifier = Modifier.size(SplitButtonDefaults.MediumTrailingButtonIconSize),
                                    imageVector = AnilibriaIcons.PlaylistPlay,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            } else {
                row { Spacer(Modifier.height(16.dp)) }

                row {
                    AlertCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = when (details.availabilityStatus) {
                                AvailabilityStatus.GeoBlocked -> stringResource(Res.string.alert_blocked_geo)
                                AvailabilityStatus.CopyrightBlocked -> stringResource(Res.string.alert_blocked_copyright)
                                AvailabilityStatus.Available -> ""
                            }
                        )
                    }
                }
            }

            if (details.notification != null) {
                row { Spacer(Modifier.height(16.dp)) }

                row {
                    NotificationCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Text(text = details.notification!!)
                    }
                }
            }

            if (details.release.description != null) {
                header(
                    title = { Text(text = stringResource(Res.string.label_description)) }
                )
                row {
                    ExpandableText(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = details.release.description!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            header(
                title = { Text(text = stringResource(Res.string.label_genres)) }
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

            if (details.releaseMembers.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_members)) }
                )
                horizontalItems(
                    items = details.releaseMembers,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { member ->
                    MemberItem(
                        releaseMember = member,
                        onClick = { /*TODO*/ }
                    )
                }
            }

            if (details.relatedReleases.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_related_releases)) }
                )
                horizontalItems(
                    items = details.relatedReleases,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) { release ->
                    AnilibriaTheme(darkTheme = false) {
                        SmallReleaseCard(
                            release = release,
                            onClick = { onReleaseClick(it.id) }
                        )
                    }
                }
            }

            row { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SuggestionRow(
    modifier: Modifier = Modifier,
    onAddToFavoritesClick: () -> Unit,
    onAlreadyWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onTelegramClick: () -> Unit,
) {
    val interactionSources = remember { List(4) { MutableInteractionSource() } }

    ButtonGroup(
        modifier = modifier,
    ) {
        LabeledIconButton(
            modifier = Modifier
                .weight(1f)
                .animateWidth(interactionSources[0]),
            onClick = onAddToFavoritesClick,
            text = stringResource(Res.string.button_add_to_favorites),
            icon = AnilibriaIcons.Filled.Star,
            interactionSource = interactionSources[0]
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f)
                .animateWidth(interactionSources[1]),
            onClick = onAlreadyWatchedClick,
            text = stringResource(Res.string.button_watched_it),
            icon = AnilibriaIcons.Checklist,
            interactionSource = interactionSources[1]
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f)
                .animateWidth(interactionSources[2]),
            onClick = onShareClick,
            text = stringResource(Res.string.button_share),
            icon = AnilibriaIcons.Share,
            interactionSource = interactionSources[2]
        )
        LabeledIconButton(
            modifier = Modifier
                .weight(1f)
                .animateWidth(interactionSources[3]),
            onClick = onTelegramClick,
            text = stringResource(Res.string.button_telegram),
            icon = AnilibriaIcons.TelegramLogo,
            interactionSource = interactionSources[3]
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
            LargeReleaseCard(null)
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
