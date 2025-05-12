package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerEvents

@Composable
fun rememberPresentationState(
    player: VideoPlayerController,
): PresentationState {
    return remember(player) { PresentationState(player) }
}

class PresentationState(player: VideoPlayerController) {
    var videoSizeDp: Size? by mutableStateOf(null)
        private set
    var coverSurface: Boolean by mutableStateOf(true)
        private set

    init {
        player.addEventListener(
            object : VideoPlayerEvents {
                override fun onVideoSizeChanged(width: Int, height: Int, pixelAspectRatio: Float) {
                    videoSizeDp = getVideoSizeDp(width, height, pixelAspectRatio)
                }

                override fun onRenderedFirstFrame() {
                    coverSurface = false
                }
            }
        )
    }

    private fun getVideoSizeDp(width: Int, height: Int, pixelAspectRatio: Float): Size? {
        var videoSize = Size(width.toFloat(), height.toFloat())
        if (videoSize.width == 0f || videoSize.height == 0f) return null
        if (pixelAspectRatio < 1.0) {
            videoSize = videoSize.copy(width = videoSize.width * pixelAspectRatio)
        } else if (pixelAspectRatio > 1.0) {
            videoSize = videoSize.copy(height = videoSize.height / pixelAspectRatio)
        }
        return videoSize
    }
}