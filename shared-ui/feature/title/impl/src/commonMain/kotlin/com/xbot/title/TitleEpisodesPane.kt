package com.xbot.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
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
import com.xbot.designsystem.components.EpisodeListItem
import com.xbot.designsystem.components.itemsIndexed
import com.xbot.designsystem.components.section
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.domain.models.Episode
import org.koin.compose.viewmodel.koinViewModel

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun TitleEpisodesPane(
    modifier: Modifier = Modifier,
    viewModel: TitleEpisodesViewModel = koinViewModel(),
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = AnilibriaIcons.ArrowBack,
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
                is TitleEpisodesScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                is TitleEpisodesScreenState.Success -> {
                    EpisodesList(
                        episodes = targetState.episodes.reversed(),
                        contentPadding = innerPadding,
                    ) { ordinal ->
                        onPlayClick(
                            targetState.releaseId,
                            targetState.episodes.size - ordinal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodesList(
    episodes: List<Episode>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onEpisodeClick: (Int) -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Custom)
    ProvideShimmer(shimmer) {
        LazyColumn(
            modifier = modifier.shimmerUpdater(shimmer),
            contentPadding = contentPadding,
            reverseLayout = false
        ) {
            section(
                footer = { Spacer(Modifier.height(16.dp)) }
            ) {
                itemsIndexed(episodes) { index, episode ->
                    EpisodeListItem(episode) {
                        onEpisodeClick(index + 1)
                    }
                }
            }
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
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }
}
