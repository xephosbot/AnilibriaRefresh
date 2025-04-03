package com.xbot.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.common.localization.toLocalizedString
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.ReleaseCardItem
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.row
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Release
import kotlinx.datetime.DayOfWeek
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScheduleScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick,
        onReleaseClick = onReleaseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleScreenContent(
    modifier: Modifier = Modifier,
    state: ScheduleScreenState,
    onBackClick: () -> Unit,
    onReleaseClick: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Schedule"
                    )
                },
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Crossfade(
            targetState = state
        ) { targetState ->
            when (targetState) {
                is ScheduleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is ScheduleScreenState.Success -> {
                    ScheduleFeed(
                        schedule = targetState.schedule,
                        contentPadding = innerPadding,
                        onReleaseClick = onReleaseClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleFeed(
    modifier: Modifier = Modifier,
    schedule: Map<DayOfWeek, List<Release>>,
    contentPadding: PaddingValues,
    onReleaseClick: (Int) -> Unit,
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    ProvideShimmer(shimmer) {
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
            columns = GridCells.Adaptive(350.dp)
        ) {
            schedule.forEach { (dayOfWeek, releases) ->
                header(
                    title = { Text(text = dayOfWeek.toLocalizedString()) }
                )
                horizontalItems(
                    items = releases,
                    contentPadding = PaddingValues(horizontal = 16.dp)
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
            Spacer(Modifier.height(contentPadding.calculateTopPadding()))
            repeat(5) {
                Header(title = { Text(text = "") })
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState(), enabled = false)
                        .padding(horizontal = 16.dp)
                ) {
                    repeat(10) {
                        ReleaseCardItem(release = null) {}
                    }
                }
            }
        }
    }
}
