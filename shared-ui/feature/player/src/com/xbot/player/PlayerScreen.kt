package com.xbot.player

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

@Composable
private fun PlayerScreenContent(
    modifier: Modifier,
    state: PlayerScreenState,
    onBackClick: () -> Unit
) {
    val player = rememberVideoPlayer()
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (!isPlaying) {
            if (state is PlayerScreenState.Success) {
                player.setUrl(state.url)
                isPlaying = true
            }
        }
    }

    VideoPlayerLayout(
        player = player,
        controls = {
            VideoPlayerController(player)
        },
        buffering = {
            CircularProgressIndicator()
        }
    )
}