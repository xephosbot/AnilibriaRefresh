package com.xbot.title

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.components.MemberItem
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.TypedCrossFade
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
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.LocalIsSinglePane
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.only
import com.xbot.domain.di.domainModule
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.models.enums.AvailabilityStatus
import com.xbot.fixtures.di.fixturesModule
import com.xbot.resources.Res
import com.xbot.resources.alert_blocked_copyright
import com.xbot.resources.alert_blocked_geo
import com.xbot.resources.button_watch_continue
import com.xbot.resources.label_members
import com.xbot.resources.label_related_releases
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.ui.AlertCard
import com.xbot.title.ui.NotificationCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isSinglePane = LocalIsSinglePane.current

    val gridState = rememberLazyGridState()
    var selected by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
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
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    visible = state is TitleScreenState.Success && (state as TitleScreenState.Success).title.episodes.isNotEmpty(),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    WatchButton(
                        onClick = {
                            (state as? TitleScreenState.Success)?.title?.release?.let { release ->
                                onPlayClick(release.id, 0)
                            }
                        }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        TypedCrossFade(
            targetState = state,
        ) { targetState ->
            when (targetState) {
                is TitleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is TitleScreenState.Success -> {
                    TitleDetails(
                        gridState = gridState,
                        isSinglePane = isSinglePane,
                        details = targetState.title,
                        contentPadding = innerPadding,
                        onPlayClick = onPlayClick,
                        onReleaseClick = onReleaseClick,
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
    isSinglePane: Boolean,
    details: ReleaseDetail,
    contentPadding: PaddingValues,
    onPlayClick: (Int, Int) -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    val columnsCount = remember {
        derivedStateOf { gridState.layoutInfo.maxSpan }
    }

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
                ) {
                    if (details.genres.isNotEmpty()) {
                        FlowRow(
                            maxLines = 1,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            details.genres.forEach { genre ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(text = genre.name) }
                                )
                            }
                        }
                    }
                    if (!isSinglePane && details.episodes.isNotEmpty()) {
                        WatchButton(
                            onClick = {
                                onPlayClick(details.release.id, 0)
                            }
                        )
                    }
                }
            }

            if (details.availabilityStatus != AvailabilityStatus.Available) {
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
                    SmallReleaseCard(
                        release = release,
                        onClick = { onReleaseClick(it.id) }
                    )
                }
            }

            if (details.episodes.isNotEmpty()) {
                header(
                    title = { Text(text = "Episodes") }
                )
                itemsIndexed(details.episodes) { index, episode ->
                    EpisodeListItem(
                        modifier = Modifier.section(
                            index,
                            details.episodes.size,
                            columnsCount.value
                        ),
                        episode = episode,
                        onClick = { onPlayClick(details.release.id, index) }
                    )
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

@Preview
@Composable
private fun TitleDetailsPanePreview() {
    AnilibriaPreview {
        KoinApplicationPreview(
            application = {
                modules(
                    domainModule,
                    fixturesModule,
                    module {
                        single { SnackbarManager }
                        single { TitleRoute("1") }
                        viewModelOf(::TitleViewModel)
                    }
                )
            }
        ) {
            TitleDetailsPane(
                onBackClick = {},
                onPlayClick = { _, _ -> },
                onReleaseClick = {}
            )
        }
    }
}
