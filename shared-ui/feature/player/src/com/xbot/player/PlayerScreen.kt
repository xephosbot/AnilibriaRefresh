package com.xbot.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.player.platform.PlatformPlayerSurface
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.modifier.resizeWithContentScale
import com.xbot.player.ui.rememberVideoPlayerController
import com.xbot.player.ui.state.rememberPlayPauseButtonState
import com.xbot.player.ui.state.rememberPresentationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.seconds

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

    val presentationState = rememberPresentationState(player)
    val scope = rememberCoroutineScope()

    LaunchedEffect(presentationState) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                println(presentationState.videoSizeDp)
                delay(1.seconds)
            }
        }
    }

    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        PlatformPlayerSurface(
            player = player,
            modifier = Modifier.resizeWithContentScale(
                contentScale = ContentScale.Fit,
                sourceSizeDp = presentationState.videoSizeDp
            )
        )

        if (presentationState.coverSurface) {
            Box(Modifier.matchParentSize().background(Color.Black))
        }

        PlayPauseButton(player)

        Button(
            modifier = modifier.align(Alignment.BottomCenter),
            onClick = {
                onBackClick()
            }
        ) {
            Text("Back")
        }
    }
}

@Composable
internal fun PlayPauseButton(player: VideoPlayerController, modifier: Modifier = Modifier) {
    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.isPlaying) AnilibriaIcons.Outlined.PlayArrow else AnilibriaIcons.Outlined.Pause
    IconButton(
        modifier = modifier.size(80.dp),
        onClick = state::onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}