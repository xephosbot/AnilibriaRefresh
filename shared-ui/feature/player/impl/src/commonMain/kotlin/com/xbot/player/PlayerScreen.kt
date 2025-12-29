package com.xbot.player

import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerLayout
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.createVideoPlayerState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SystemBarsEffect()

    PlayerScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick
    )
}

@Composable
fun rememberVideoPlayerState(): VideoPlayerState {
    val playerState = retain { createVideoPlayerState() }
    RetainedEffect(Unit) {
        onRetire {
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
    val pipController = rememberPictureInPictureController(player)

    LaunchedEffect(state) {
        if (!player.hasMedia) {
            if (state.currentEpisode != null) {
                player.openUri(state.currentEpisode.let { it.hls1080 ?: it.hls720 ?: it.hls480 ?: ""})
            }
        }
    }

    VideoPlayerLayout(
        modifier = modifier.keepScreenOn().then(pipController.modifier),
        player = player,
        controls = {
            if (!pipController.isInPictureInPictureMode) {
                VideoPlayerController(
                    player = player,
                    title = state.currentEpisode?.name.orEmpty(),
                    buffering = {
                        ContainedLoadingIndicator()
                    },
                    onClickBack = onBackClick
                )
            }
        },
    )
}
