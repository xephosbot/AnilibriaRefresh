package com.xbot.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.LazyColumnWithStickyHeader
import com.xbot.designsystem.components.MediumReleaseCard
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.plus
import com.xbot.localization.DayOfWeekStyle
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.label_schedule
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
internal fun SchedulePane(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel(),
    showBackButton: Boolean,
    onReleaseClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SchedulePane(
        modifier = modifier,
        state = state,
        showBackButton = showBackButton,
        onReleaseClick = onReleaseClick,
        onBackClick = onBackClick,
    )
}

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun SchedulePane(
    modifier: Modifier = Modifier,
    state: ScheduleScreenState,
    showBackButton: Boolean,
    onReleaseClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(Res.string.label_schedule)) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick,
                            shapes = IconButtonDefaults.shapes(),
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        Crossfade(
            targetState = state
        ) { targetState ->
            when (targetState) {
                is ScheduleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is ScheduleScreenState.Success -> {
                    ScheduleContent(
                        state = targetState,
                        contentPadding = innerPadding,
                        onReleaseClick = onReleaseClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    state: ScheduleScreenState.Success,
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        LazyColumnWithStickyHeader(
            items = state.schedule,
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding.plus(PaddingValues(16.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            stickyHeader = { date ->
                DateItem(
                    modifier = Modifier.padding(start = 16.dp),
                    date = date
                )
            },
            itemContent = { schedule ->
                MediumReleaseCard(
                    modifier = Modifier.fillMaxWidth(),
                    release = schedule.release,
                    onClick = { onReleaseClick(schedule.release.id) },
                ) {
                    EpisodeListItem(
                        episode = schedule.publishedReleaseEpisode,
                        onClick = {}
                    )
                }
            }
        )
    }
}

@Composable
private fun DateItem(
    date: LocalDate,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(containerColor)
            .defaultMinSize(minWidth = 40.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Text(
                text = date.dayOfWeek.toLocalizedString(style = DayOfWeekStyle.SHORT),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = date.day.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)
    val placeholderItems = remember {
        mutableMapOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date to (0..5).toList())
    }

    ProvideShimmer(shimmer) {
        LazyColumnWithStickyHeader(
            items = placeholderItems,
            modifier = modifier,
            contentPadding = contentPadding.plus(PaddingValues(16.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
            stickyHeader = { date ->
                DateItem(
                    modifier = Modifier.padding(start = 16.dp),
                    date = date
                )
            },
            itemContent = {
                MediumReleaseCard(
                    modifier = Modifier.fillMaxWidth(),
                    release = null,
                    onClick = {},
                )
            }
        )
    }
}
