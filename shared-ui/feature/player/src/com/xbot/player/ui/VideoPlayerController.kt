package com.xbot.player.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.player.ui.state.rememberPlayPauseButtonState
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerController(
    player: VideoPlayer,
    modifier: Modifier = Modifier
) {
    var isControllerShowing by rememberSaveable { mutableStateOf(true) }

    Crossfade(
        targetState = isControllerShowing,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { isControllerShowing = !isControllerShowing }
            )
    ) { isShowing ->
        if (isShowing) {
            var hideEffectReset by remember { mutableStateOf(0) }

            LaunchedEffect(hideEffectReset) {
                delay(3000)
                isControllerShowing = false
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                PlayPauseButton(player)
            }
        }
    }
}

@Composable
internal fun PlayPauseButton(player: VideoPlayer, modifier: Modifier = Modifier) {
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