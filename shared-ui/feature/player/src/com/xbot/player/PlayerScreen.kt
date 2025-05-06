package com.xbot.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.platform.PlatformPlayerSurface
import com.xbot.player.ui.rememberVideoPlayerController
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
    val player = rememberVideoPlayerController()
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (!isPlaying) {
            if (state is PlayerScreenState.Success) {
                player.setUrl(state.url)
                player.play()
                isPlaying = true
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        PlatformPlayerSurface(
            player = player,
            modifier = modifier.fillMaxSize(),
        )

        Button(
            onClick = {
                onBackClick()
            }
        ) {
            Text("Back")
        }
    }
}