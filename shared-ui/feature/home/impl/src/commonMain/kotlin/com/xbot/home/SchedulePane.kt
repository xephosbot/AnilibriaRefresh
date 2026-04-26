package com.xbot.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.AsyncResult
import com.xbot.common.getOrElse
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.LazyColumnWithStickyHeader
import com.xbot.designsystem.components.MediumReleaseCard
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.theme.LocalMargins
import com.xbot.designsystem.theme.asPaddingValues
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.fixtures.ScheduleFixtures
import com.xbot.domain.models.Release
import com.xbot.localization.DayOfWeekStyle
import com.xbot.localization.UiText
import com.xbot.localization.localizedMessage
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import com.xbot.resources.label_schedule
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
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
    viewModel: HomeViewModel = koinViewModel(),
    showBackButton: Boolean,
    onReleaseClick: (Release) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeScreenSideEffect.ShowErrorMessage -> {
                SnackbarManager.showMessage(
                    title = sideEffect.error.localizedMessage(),
                    action = MessageAction(
                        title = UiText.Text(Res.string.button_retry),
                        action = sideEffect.onRetry,
                    )
                )
            }
        }
    }

    SchedulePaneContent(
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
private fun SchedulePaneContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    showBackButton: Boolean,
    onReleaseClick: (Release) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(text = stringResource(Res.string.label_schedule)) },
                navigationIcon = {
                    if (showBackButton) {
                        FilledTonalIconButton(
                            modifier = Modifier.padding(start = 6.dp),
                            onClick = onBackClick,
                            shapes = IconButtonDefaults.shapes(),
                            colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colorScheme.surfaceContainerHighest)
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer)
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        ScheduleContent(
            scheduleWeek = state.scheduleWeek,
            contentPadding = innerPadding,
            onReleaseClick = onReleaseClick
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    scheduleWeek: ScheduleWeek,
    contentPadding: PaddingValues,
    onReleaseClick: (Release) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        LazyColumnWithStickyHeader(
            items = scheduleWeek.days.getOrElse {
                val baseDate =
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                (0..6).associate { offset ->
                    val date = baseDate.plus(offset, DateTimeUnit.DAY)
                    date to listOf(null, null)
                }
            },
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding + LocalMargins.current.asPaddingValues(),
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
                    release = schedule?.release,
                    onClick = { schedule?.release?.let(onReleaseClick) },
                ) {
                    schedule?.let {
                        EpisodeListItem(
                            episode = it.toEpisode(),
                            onClick = {}
                        )
                    }
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
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(containerColor)
            .defaultMinSize(minWidth = 40.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.dayOfWeek.toLocalizedString(style = DayOfWeekStyle.SHORT),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
        Text(
            text = date.day.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun SchedulePanePreview(
    @PreviewParameter(ScheduleScreenStateProvider::class) state: HomeScreenState
) {
    AnilibriaPreview {
        SchedulePaneContent(
            state = state,
            showBackButton = true,
            onReleaseClick = {},
            onBackClick = {}
        )
    }
}

private class ScheduleScreenStateProvider : PreviewParameterProvider<HomeScreenState> {
    override val values = sequenceOf(
        HomeScreenState(
            scheduleWeek = ScheduleWeek(
                days = AsyncResult.Success(
                    run {
                        val baseDate =
                            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        (0..6).associate { offset ->
                            val date = baseDate.plus(offset, DateTimeUnit.DAY)
                            date to ScheduleFixtures.all
                        }
                    }
                )
            )
        ),
        HomeScreenState(
            scheduleWeek = ScheduleWeek()
        )
    )
}
