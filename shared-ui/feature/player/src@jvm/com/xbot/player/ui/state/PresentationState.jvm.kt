package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Size
import com.xbot.player.platform.VLCPlayerController
import com.xbot.player.ui.VideoPlayerController
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter

@Composable
actual fun rememberPresentationState(
    controller: VideoPlayerController,
    keepContentOnReset: Boolean
): PresentationState {
    check(controller is VLCPlayerController) { "Player is not an instance of VLCPlayerController" }

    val presentationState = remember { DefaultPresentationState(keepContentOnReset) }
    LaunchedEffect(controller) { presentationState.observe(controller) }
    return presentationState
}

internal class DefaultPresentationState(keepContentOnReset: Boolean = false) : PresentationState {
    private var _videoSizeDp: Size? by mutableStateOf(null)
    private var _coverSurface: Boolean by mutableStateOf(true)

    override val videoSizeDp: Size? get() = _videoSizeDp
    override val coverSurface: Boolean get() = _coverSurface
    override var keepContentOnReset: Boolean = keepContentOnReset
        set(value) {
            if (value != field) {
                field = value
                updateSurfaceVisibility()
            }
        }

    private var controller: VLCPlayerController? = null

    suspend fun observe(controller: VLCPlayerController) {
        try {
            this.controller = controller

            updateVideoSize(controller.player)
            updateSurfaceVisibility()

            controller.player.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
                override fun videoOutput(mediaPlayer: MediaPlayer?, newCount: Int) {
                    if (newCount > 0) {
                        updateVideoSize(mediaPlayer)
                        _coverSurface = false
                    } else if (!keepContentOnReset) {
                        _coverSurface = true
                    }
                }

                override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
                    if (!keepContentOnReset) {
                        _coverSurface = true
                    }
                }

                override fun stopped(mediaPlayer: MediaPlayer?) {
                    if (!keepContentOnReset) {
                        _coverSurface = true
                    }
                }
            })

            snapshotFlow { controller.surface.composeBitmap.value }
                .collect {
                    updateVideoSize(controller.player)
                }
        } finally {
            this.controller = null
        }
    }

    private fun updateVideoSize(player: MediaPlayer?) {
        val videoTrack = player?.media()?.info()?.videoTracks()?.firstOrNull() ?: return

        val width = videoTrack.width().toFloat()
        val height = videoTrack.height().toFloat()

        if (width > 0 && height > 0) {
            val pixelAspectRatio = videoTrack.sampleAspectRatio().toFloat() / videoTrack.sampleAspectRatio().toFloat()

            val videoSize = if (pixelAspectRatio < 1.0f) {
                Size(width * pixelAspectRatio, height)
            } else if (pixelAspectRatio > 1.0f) {
                Size(width, height / pixelAspectRatio)
            } else {
                Size(width, height)
            }

            if (videoSize != _videoSizeDp) _videoSizeDp = videoSize
        }
    }

    private fun updateSurfaceVisibility() {
        val controller = this.controller ?: return

        val hasMedia = controller.player.media().isValid
        val hasVideoTracks = controller.player.media()?.info()?.videoTracks()?.isNotEmpty() ?: false

        if (!keepContentOnReset && (!hasMedia || !hasVideoTracks)) {
            _coverSurface = true
        }
    }
}