package com.xbot.title

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.itemsIndexed
import com.xbot.designsystem.components.row
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.MoreVert
import com.xbot.designsystem.icons.PlayArrow
import com.xbot.designsystem.icons.Star
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.modifier.verticalParallax
import com.xbot.designsystem.theme.LocalMargins
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.LocalIsSinglePane
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleaseDetailsExtended
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.fixtures.data.getReleaseDetailMock
import com.xbot.resources.Res
import com.xbot.resources.alert_blocked_copyright
import com.xbot.resources.alert_blocked_geo
import com.xbot.resources.button_watch_continue
import com.xbot.resources.label_episodes
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
    viewModel: TitleViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleDetailsPaneContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onPlayClick = onPlayClick,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TitleDetailsPaneContent(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    onAction: (TitleScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val isSinglePane = LocalIsSinglePane.current
    val gridState = rememberLazyGridState()
    var selected by remember { mutableStateOf(false) }

    val shimmer = rememberShimmer(ShimmerBounds.Custom)

    ProvideShimmer(shimmer) {
        Scaffold(
            modifier = modifier
                .shimmerUpdater(shimmer),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f),
                                )
                            )
                        ),
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
                                checkedContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.inverseSurface),
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
                                IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)
                            ),
                            shapes = IconButtonDefaults.shapes()
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.MoreVert,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                )
            },
            bottomBar = {
                if (isSinglePane) {
                    AnimatedVisibility(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f),
                                        MaterialTheme.colorScheme.surfaceContainer,
                                    )
                                )
                            )
                            .padding(horizontal = LocalMargins.current.horizontal + 8.dp, vertical = 16.dp),
                        visible = state.isWatchButtonVisible,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        WatchButton(
                            onClick = {
                                state.release.details.release?.let { onPlayClick(it.id, 0) }
                            }
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            TitleDetails(
                gridState = gridState,
                isSinglePane = isSinglePane,
                state = state,
                contentPadding = innerPadding,
                onPlayClick = onPlayClick,
                onReleaseClick = onReleaseClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TitleDetails(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    isSinglePane: Boolean,
    state: TitleScreenState,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val columnsCount = remember {
        derivedStateOf { gridState.layoutInfo.maxSpan }
    }
    val overscrollEffect = rememberOverscrollEffect()
    val horizontalMargin = LocalMargins.current.horizontal

    Feed(
        modifier = modifier,
        state = gridState,
        columns = GridCells.Adaptive(400.dp),
        contentPadding = contentPadding.only(WindowInsetsSides.Bottom),
        overscrollEffect = overscrollEffect,
    ) {
        row {
            LargeReleaseCard(
                modifier = Modifier.verticalParallax(gridState),
                contentModifier = Modifier.animateContentSize(),
                release = state.release.details.release,
                contentPadding = PaddingValues(horizontal = horizontalMargin) + contentPadding.only(WindowInsetsSides.Horizontal),
                overscrollEffect = overscrollEffect,
            ) {
                AnimatedVisibility(
                    visible = state.genres.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    ChipGroup(
                        items = state.genres,
                        maxLines = 1,
                        contentPadding = PaddingValues(0.dp)
                    ) { genre ->
                        AssistChip(
                            onClick = {},
                            label = { Text(text = genre?.name.orEmpty()) }
                        )
                    }
                }
                if (!isSinglePane && state.isWatchButtonVisible) {
                    WatchButton(
                        onClick = {
                            state.release.details.release?.let { onPlayClick(it.id, 0) }
                        }
                    )
                }
            }
        }

        row {
            AnimatedVisibility(
                visible = state.blockedStatus != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    AlertCard(
                        modifier = Modifier.padding(horizontal = horizontalMargin),
                    ) {
                        Text(
                            text = when (state.blockedStatus) {
                                AvailabilityStatus.GeoBlocked -> stringResource(Res.string.alert_blocked_geo)
                                AvailabilityStatus.CopyrightBlocked -> stringResource(Res.string.alert_blocked_copyright)
                                else -> ""
                            }
                        )
                    }
                }
            }
        }

        row {
            AnimatedVisibility(
                visible = state.notification != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    NotificationCard(
                        modifier = Modifier.padding(horizontal = horizontalMargin),
                    ) {
                        Text(text = state.notification.orEmpty())
                    }
                }
            }
        }

        if (state.releaseMembers.isNotEmpty()) {
            header(
                title = { Text(text = stringResource(Res.string.label_members)) },
                contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
            )
            horizontalItems(
                items = state.releaseMembers,
                contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
            ) { member ->
                MemberItem(
                    releaseMember = member,
                    onClick = { /*TODO*/ }
                )
            }
        }

        if (state.relatedReleases.isNotEmpty()) {
            header(
                title = { Text(text = stringResource(Res.string.label_related_releases)) },
                contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
            )
            horizontalItems(
                items = state.relatedReleases,
                contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
            ) { release ->
                SmallReleaseCard(
                    release = release,
                    onClick = onReleaseClick,
                )
            }
        }

        if (state.episodes.isNotEmpty()) {
            header(
                title = { Text(text = stringResource(Res.string.label_episodes)) },
                contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
            )
            itemsIndexed(state.episodes) { index, episode ->
                val release = state.release.details.release
                EpisodeListItem(
                    modifier = Modifier.section(index, state.episodes.size, columnsCount.value),
                    episode = episode,
                    onClick = {
                        if (release != null) {
                            onPlayClick(release.id, index)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WatchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonDefaults.MediumContainerHeight),
        onClick = onClick,
        contentPadding = ButtonDefaults.MediumContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Icon(
            modifier = Modifier.size(ButtonDefaults.MediumIconSize),
            imageVector = AnilibriaIcons.Filled.PlayArrow,
            contentDescription = null
        )
        Spacer(Modifier.width(ButtonDefaults.MediumIconSpacing))
        ProvideTextStyle(
            LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            Text(
                text = stringResource(Res.string.button_watch_continue),
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun TitleDetailsPanePreview(
    @PreviewParameter(TitleScreenStateProvider::class) state: TitleScreenState
) {
    AnilibriaPreview {
        TitleDetailsPaneContent(
            state = state,
            onAction = {},
            onBackClick = {},
            onPlayClick = { _, _ -> },
            onReleaseClick = {}
        )
    }
}

private class TitleScreenStateProvider : PreviewParameterProvider<TitleScreenState> {
    override val values = sequenceOf(
        TitleScreenState(
            release = ReleaseDetailsExtended.create(
                release = getReleaseDetailMock(1).details.release
            )
        ),
        TitleScreenState(
            release = ReleaseDetailsExtended.create(
                release = getReleaseDetailMock(1).details.release,
                details = getReleaseDetailMock(1).details,
                relatedReleases = emptyList()
            )
        )
    )
}

private val TitleScreenState.isWatchButtonVisible: Boolean
    get() = release.details.episodes.firstOrNull() != null

private val TitleScreenState.blockedStatus: AvailabilityStatus?
    get() = release.details.availabilityStatus.takeIf { it != AvailabilityStatus.Available }

private val TitleScreenState.notification: String?
    get() = release.details.notification

private val TitleScreenState.genres
    get() = release.details.genres

private val TitleScreenState.releaseMembers
    get() = release.details.releaseMembers

private val TitleScreenState.relatedReleases
    get() = release.relatedReleases

private val TitleScreenState.episodes
    get() = release.details.episodes
