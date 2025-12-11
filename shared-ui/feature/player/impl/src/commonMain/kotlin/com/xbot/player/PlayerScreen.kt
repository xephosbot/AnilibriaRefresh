package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
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

@Composable
fun rememberVideoPlayerState(): VideoPlayerState {
    val playerState = retain { VideoPlayerState() }
    DisposableEffect(Unit) {
        onDispose {
            playerState.stop()
            playerState.dispose()
        }
    }
    return playerState
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayerScreenContent(
    modifier: Modifier,
    state: PlayerScreenState,
    onBackClick: () -> Unit
) {
    val player = rememberVideoPlayerState()

    LaunchedEffect(state) {
        if (!player.hasMedia) {
            if (state.currentEpisode != null) {
                player.openUri(state.currentEpisode.let { it.hls1080 ?: it.hls720 ?: it.hls480 ?: ""})
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
        modifier = modifier,
    )
}