package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
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

class PlayPauseButtonState(private val player: VideoPlayer) {
    var isPlaying: Boolean by mutableStateOf(false)
        private set

    init {
        player.addEventListener(
            object : VideoPlayerEvents {
                override fun onPlaybackStateChanged(state: PlaybackState) {
                    when (state) {
                        PlaybackState.PLAYING -> { isPlaying = true }
                        PlaybackState.PAUSED -> { isPlaying = false }
                        PlaybackState.STOPPED -> { isPlaying = false }
                        else -> {}
                    }
                }
            }
        )
    }

    fun onClick() {
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }
}