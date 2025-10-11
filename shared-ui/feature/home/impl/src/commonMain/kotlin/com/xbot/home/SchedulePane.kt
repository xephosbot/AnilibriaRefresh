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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.Feed
import com.xbot.designsystem.components.Header
import com.xbot.designsystem.components.SmallReleaseCard
import com.xbot.designsystem.components.header
import com.xbot.designsystem.components.horizontalItems
import com.xbot.designsystem.components.row
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.designsystem.utils.only
import com.xbot.localization.toLocalizedString
import com.xbot.resources.Res
import com.xbot.resources.label_schedule
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

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
        containerColor = MaterialTheme.colorScheme.surface
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
        Feed(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
            columns = GridCells.Adaptive(350.dp)
        ) {
            state.schedule.forEach { (dayOfWeek, releases) ->
                header(
                    title = { Text(text = dayOfWeek.toLocalizedString()) }
                )
                horizontalItems(
                    items = releases,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { release ->
                    AnilibriaTheme(darkTheme = false) {
                        SmallReleaseCard(
                            release = release.release,
                            onClick = { onReleaseClick(it.id) }
                        )
                    }
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
                        SmallReleaseCard(release = null) {}
                    }
                }
            }
        }
    }
}
