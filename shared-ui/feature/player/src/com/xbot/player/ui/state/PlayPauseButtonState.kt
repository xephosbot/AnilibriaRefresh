package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.xbot.player.ui.PlaybackState
import com.xbot.player.ui.VideoPlayer
import com.xbot.player.ui.VideoPlayerEvents

@Composable
fun rememberPlayPauseButtonState(
    player: VideoPlayer,
): PlayPauseButtonState {
    return remember(player) { PlayPauseButtonState(player) }
}

class PlayPauseButtonState(private val player: VideoPlayer) : RememberObserver {
    var isPlaying: Boolean by mutableStateOf(false)
        private set

    private val listener = object : VideoPlayerEvents {
        override fun onPlaybackStateChanged(state: PlaybackState) {
            when (state) {
                PlaybackState.PLAYING -> { isPlaying = true }
                PlaybackState.PAUSED -> { isPlaying = false }
                PlaybackState.STOPPED -> { isPlaying = false }
                else -> {}
            }
        }
    }

    override fun onRemembered() = player.addEventListener(listener)

    override fun onForgotten() = player.removeEventListener(listener)

    override fun onAbandoned() = Unit

    fun onClick() {
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }
}