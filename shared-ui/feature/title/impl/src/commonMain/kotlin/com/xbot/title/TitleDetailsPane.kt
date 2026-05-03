package com.xbot.title

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.AsyncResult
import com.xbot.common.copyText
import com.xbot.common.getOrElse
import com.xbot.common.getOrNull
import com.xbot.designsystem.components.ChipGroup
import com.xbot.designsystem.components.ContextMenu
import com.xbot.designsystem.components.ContextMenuItem
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.SectionDefaults
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
import com.xbot.designsystem.utils.AnilibriaPreviewWrapper
import com.xbot.designsystem.utils.LocalIsSinglePane
import com.xbot.designsystem.utils.LocalNavSharedTransitionScope
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.build
import com.xbot.designsystem.utils.only
import com.xbot.domain.fixtures.ReleaseFixtures
import com.xbot.domain.fixtures.createReleaseDetails
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.domain.models.hlsUrl
import com.xbot.formatters.localizedMessage
import com.xbot.resources.Res
import com.xbot.resources.StringResource
import com.xbot.resources.alert_blocked_copyright
import com.xbot.resources.alert_blocked_geo
import com.xbot.resources.button_copy
import com.xbot.resources.button_retry
import com.xbot.resources.button_watch_continue
import com.xbot.resources.label_episodes
import com.xbot.resources.label_members
import com.xbot.resources.label_related_releases
import com.xbot.resources.message_copied_to_clipboard
import com.xbot.title.ui.AlertCard
import com.xbot.title.ui.NotificationCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

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
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TitleScreenSideEffect.ShowErrorMessage -> {
                SnackbarManager.build()
                    .setTitle(sideEffect.error.localizedMessage())
                    .setAction(StringResource.Text(Res.string.button_retry)) {
                        sideEffect.onRetry()
                    }
                    .show()
            }
        }
    }

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
    var isWatchButtonOnScreen by remember { mutableStateOf(true) }

    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val sharedTransitionScope = LocalNavSharedTransitionScope.current

    with(sharedTransitionScope) {
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
                    val details = state.details.getOrNull()
                    val hasEpisodes = details?.episodes?.isNotEmpty() == true

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
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        visible = isSinglePane && !isWatchButtonOnScreen && hasEpisodes,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        WatchButton(
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = "watch_button"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                                ),
                            onClick = {
                                state.initialRelease?.let { release ->
                                    onPlayClick(release.id, 0)
                                }
                            }
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) { innerPadding ->
                TitleDetails(
                    state = state,
                    gridState = gridState,
                    isSinglePane = isSinglePane,
                    isWatchButtonOnScreen = isWatchButtonOnScreen,
                    onWatchButtonVisibilityChanged = { visible ->
                        if (!visible) {
                            isWatchButtonOnScreen = false
                        }
                    },
                    contentPadding = innerPadding,
                    onPlayClick = onPlayClick,
                    onReleaseClick = onReleaseClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TitleDetails(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    gridState: LazyGridState,
    isSinglePane: Boolean,
    isWatchButtonOnScreen: Boolean,
    onWatchButtonVisibilityChanged: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Release) -> Unit,
) {
    val columnsCount = remember {
        derivedStateOf { gridState.layoutInfo.maxSpan }
    }
    val horizontalMargin = 16.dp

    //TODO: Change to LocalClipboard
    val clipboard = LocalClipboardManager.current
    var showEpisodeMenu by remember { mutableStateOf<String?>(null) }

    with(LocalNavSharedTransitionScope.current) {
        Feed(
            modifier = modifier,
            state = gridState,
            columns = GridCells.Adaptive(400.dp),
            contentPadding = contentPadding.only(WindowInsetsSides.Bottom),
        ) {
            row {
                LargeReleaseCard(
                    modifier = Modifier.verticalParallax(gridState),
                    contentModifier = Modifier.animateContentSize(),
                    release = state.initialRelease,
                    contentPadding = PaddingValues(horizontal = horizontalMargin) + contentPadding.only(WindowInsetsSides.Horizontal),
                ) {
                    AnimatedVisibility(
                        visible = (state.details.getOrNull()?.genres ?: emptyList()).isNotEmpty(),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        ChipGroup(
                            items = state.details.getOrNull()?.genres ?: emptyList(),
                            maxLines = 1,
                            contentPadding = PaddingValues(0.dp)
                        ) { genre ->
                            AssistChip(
                                onClick = {},
                                label = { Text(text = genre?.name.orEmpty()) }
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = !isSinglePane || isWatchButtonOnScreen,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        WatchButton(
                            modifier = Modifier
                                .onVisibilityChanged(callback = onWatchButtonVisibilityChanged)
                                .sharedBounds(
                                    rememberSharedContentState(key = "watch_button"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                                ),
                            onClick = {
                                state.initialRelease?.let { release ->
                                    onPlayClick(release.id, 0)
                                }
                            }
                        )
                    }
                }
            }

            row {
                val alertText = when (state.details.getOrNull()?.availabilityStatus) {
                    AvailabilityStatus.GeoBlocked -> stringResource(Res.string.alert_blocked_geo)
                    AvailabilityStatus.CopyrightBlocked -> stringResource(Res.string.alert_blocked_copyright)
                    else -> null
                }
                AnimatedVisibility(
                    visible = alertText != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(Modifier.height(16.dp))
                        AlertCard(
                            modifier = Modifier.padding(horizontal = horizontalMargin),
                        ) {
                            Text(text = alertText.orEmpty())
                        }
                    }
                }
            }

            row {
                AnimatedVisibility(
                    visible = state.details.getOrNull()?.notification != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(Modifier.height(16.dp))
                        NotificationCard(
                            modifier = Modifier.padding(horizontal = horizontalMargin),
                        ) {
                            Text(text = state.details.getOrNull()?.notification.orEmpty())
                        }
                    }
                }
            }

            if (state.details.getOrNull()?.releaseMembers?.isNotEmpty() == true) {
                header(
                    title = { Text(text = stringResource(Res.string.label_members)) },
                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
                )
                horizontalItems(
                    items = state.details.getOrNull()?.releaseMembers ?: emptyList(),
                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
                ) { member ->
                    MemberItem(
                        releaseMember = member,
                        onClick = { /*TODO*/ }
                    )
                }
            }

            if (state.relatedReleases.getOrElse { emptyList() }.isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_related_releases)) },
                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
                )
                horizontalItems(
                    items = state.relatedReleases.getOrElse { emptyList() },
                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
                ) { release ->
                    SmallReleaseCard(
                        release = release,
                        onClick = onReleaseClick,
                    )
                }
            }

            if ((state.details.getOrNull()?.episodes ?: emptyList()).isNotEmpty()) {
                header(
                    title = { Text(text = stringResource(Res.string.label_episodes)) },
                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal),
                )
                itemsIndexed(
                    state.details.getOrNull()?.episodes ?: emptyList()
                ) { index, episode ->
                    val copyLabel = stringResource(Res.string.button_copy)
                    ContextMenu(
                        showMenu = showEpisodeMenu == episode.id,
                        onDismiss = { showEpisodeMenu = null },
                        items = remember(episode, index, copyLabel) {
                            listOf(
                                ContextMenuItem(
                                    icon = AnilibriaIcons.Filled.PlayArrow,
                                    label = "Смотреть",
                                    onClick = {
                                        state.initialRelease?.let { onPlayClick(it.id, index) }
                                    }
                                ),
                                ContextMenuItem(
                                    icon = AnilibriaIcons.Filled.Star,
                                    label = copyLabel,
                                    onClick = {
                                        clipboard.copyText(episode.hlsUrl)
                                        SnackbarManager.build()
                                            .setTitle(StringResource.Text(Res.string.message_copied_to_clipboard))
                                            .show()
                                    }
                                ),
                            )
                        }
                    ) {
                        EpisodeListItem(
                            modifier = Modifier.section(
                                index = index,
                                itemsCount = (state.details.getOrNull()?.episodes ?: emptyList()).size,
                                columnsCount = columnsCount.value,
                                sectionSpacing = SectionDefaults.spacing(
                                    contentPadding = contentPadding.only(WindowInsetsSides.Horizontal)
                                )
                            ),
                            episode = episode,
                            onLongClick = {
                                showEpisodeMenu = episode.id
                            },
                            onClick = {
                                state.initialRelease?.let { release ->
                                    onPlayClick(release.id, index)
                                }
                            }
                        )
                    }
                }
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
@PreviewWrapper(AnilibriaPreviewWrapper::class)
@Composable
private fun TitleDetailsPanePreview(
    @PreviewParameter(TitleScreenStateProvider::class) state: TitleScreenState
) {
    TitleDetailsPaneContent(
        state = state,
        onAction = {},
        onBackClick = {},
        onPlayClick = { _, _ -> },
        onReleaseClick = {}
    )
}

private class TitleScreenStateProvider : PreviewParameterProvider<TitleScreenState> {
    override val values = sequenceOf(
        TitleScreenState(),
        TitleScreenState(
            initialRelease = ReleaseFixtures.frieren,
            details = AsyncResult.Success(createReleaseDetails(release = ReleaseFixtures.frieren)),
            relatedReleases = AsyncResult.Success(ReleaseFixtures.all),
        )
    )
}
