package com.xbot.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.components.LargeReleaseCard
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.modifier.shimmerUpdater
import com.xbot.designsystem.utils.only
import com.xbot.domain.models.Episode
import com.xbot.designsystem.components.EpisodeListItem

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
context(scope: ThreePaneScaffoldPaneScope)
internal fun TitleEpisodesPane(
    modifier: Modifier = Modifier,
    state: TitleScreenState,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onPlayClick: (Int, Int) -> Unit,
) {
    with(scope) {
        AnimatedPane(modifier = modifier) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            if (showBackButton) {
                                IconButton(
                                    onClick = onBackClick
                                ) {
                                    Icon(
                                        imageVector = AnilibriaIcons.Outlined.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Crossfade(
                    targetState = state
                ) { targetState ->
                    when (targetState) {
                        is TitleScreenState.Loading -> LoadingScreen(contentPadding = innerPadding)
                        is TitleScreenState.Success -> {
                            EpisodesList(
                                episodes = targetState.title.episodes.reversed(),
                                contentPadding = innerPadding,
                            ) { ordinal ->
                                onPlayClick(
                                    targetState.title.release.id,
                                    targetState.title.episodes.size - ordinal
                                )
                            }
                        }
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
            itemsIndexed(episodes) { index, episode ->
                Column(Modifier.padding(horizontal = 16.dp)) {
                    EpisodeListItem(episode) {
                        onEpisodeClick(index + 1)
                    }
                    if (index < episodes.size - 1) {
                        Spacer(Modifier.height(16.dp))
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
        Column(
            modifier = modifier
                .padding(contentPadding.only(WindowInsetsSides.Horizontal))
                .verticalScroll(rememberScrollState(), enabled = false)
        ) {
            LargeReleaseCard(null)
        }
    }
}
