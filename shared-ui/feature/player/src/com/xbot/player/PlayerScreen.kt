package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import com.xbot.player.ui.rememberVideoPlayer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlayerScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayerScreenContent(
    modifier: Modifier,
    state: PlayerScreenState,
    onBackClick: () -> Unit
) {
    val player = rememberVideoPlayer()

    LaunchedEffect(state) {
        if (!player.state.value.hasPlaylist) {
            if (state is PlayerScreenState.Success) {
                player.setUrls(state.episodes.map { it.hls1080 ?: it.hls720 ?: it.hls480 ?: ""})
            }
        }
    }

    VideoPlayerLayout(
        player = player,
        controls = {
            VideoPlayerController(
                player = player,
                buffering = {
                    ContainedLoadingIndicator()
                },
                onClickBack = onBackClick
            )
        },
    )
}