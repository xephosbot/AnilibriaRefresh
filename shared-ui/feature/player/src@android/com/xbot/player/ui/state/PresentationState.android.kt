package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.VideoSize
import androidx.media3.common.listen
import com.xbot.player.platform.ExoPlayerController
import com.xbot.player.ui.VideoPlayerController

@Composable
actual fun rememberPresentationState(
    controller: VideoPlayerController,
    keepContentOnReset: Boolean
): PresentationState {
    check(controller is ExoPlayerController) { "Player is not an instance of ExoPlayerController" }

    val presentationState = remember { DefaultPresentationState(keepContentOnReset) }
    LaunchedEffect(controller) { presentationState.observe(controller.exoPlayer) }
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
                maybeHideSurface(player)
            }
        }

    private var player: Player? = null
    private var lastPeriodUidWithTracks: Any? = null

    suspend fun observe(player: Player?) {
        try {
            this.player = player
            this._videoSizeDp = getVideoSizeDp(player)
            maybeHideSurface(player)
            player?.listen { events ->
                if (events.contains(Player.EVENT_VIDEO_SIZE_CHANGED)) {
                    if (videoSize != VideoSize.UNKNOWN && playbackState != Player.STATE_IDLE) {
                        this@DefaultPresentationState._videoSizeDp = getVideoSizeDp(player)
                    }
                }
                if (events.contains(Player.EVENT_RENDERED_FIRST_FRAME)) {
                    // open shutter, video available
                    _coverSurface = false
                }
                if (events.contains(Player.EVENT_TRACKS_CHANGED)) {
                    if (!shouldKeepSurfaceVisible(player)) {
                        maybeHideSurface(player)
                    }
                }
            }
        } finally {
            this.player = null
        }
    }

    private fun getVideoSizeDp(player: Player?): Size? {
        player ?: return null
        var videoSize = Size(player.videoSize.width.toFloat(), player.videoSize.height.toFloat())
        if (videoSize.width == 0f || videoSize.height == 0f) return null

        val par = player.videoSize.pixelWidthHeightRatio
        if (par < 1.0) {
            videoSize = videoSize.copy(width = videoSize.width * par)
        } else if (par > 1.0) {
            videoSize = videoSize.copy(height = videoSize.height / par)
        }
        return videoSize
    }

    private fun maybeHideSurface(player: Player?) {
        if (player != null) {
            val hasTracks =
                player.isCommandAvailable(Player.COMMAND_GET_TRACKS) && !player.currentTracks.isEmpty
            if (!keepContentOnReset && !hasTracks) {
                _coverSurface = true
            }
            if (hasTracks && !hasSelectedVideoTrack(player)) {
                _coverSurface = true
            }
        } else {
            _coverSurface = coverSurface || !keepContentOnReset
        }
    }

    private fun shouldKeepSurfaceVisible(player: Player): Boolean {
        // Suppress the shutter if transitioning to an unprepared period within the same window. This
        // is necessary to avoid closing the shutter (i.e covering the surface) when such a transition
        // occurs. See: https://github.com/google/ExoPlayer/issues/5507.
        val timeline =
            if (player.isCommandAvailable(Player.COMMAND_GET_TIMELINE)) player.currentTimeline
            else Timeline.EMPTY

        if (timeline.isEmpty) {
            lastPeriodUidWithTracks = null
            return false
        }

        val period = Timeline.Period()
        if (player.isCommandAvailable(Player.COMMAND_GET_TRACKS) && !player.currentTracks.isEmpty) {
            lastPeriodUidWithTracks =
                timeline.getPeriod(player.currentPeriodIndex, period, /* setIds= */ true).uid
        } else
            lastPeriodUidWithTracks?.let {
                val lastPeriodIndexWithTracks = timeline.getIndexOfPeriod(it)
                if (lastPeriodIndexWithTracks != C.INDEX_UNSET) {
                    val lastWindowIndexWithTracks =
                        timeline.getPeriod(lastPeriodIndexWithTracks, period).windowIndex
                    if (player.currentMediaItemIndex == lastWindowIndexWithTracks) {
                        // We're in the same media item, keep the surface visible, don't show the shutter.
                        return true
                    }
                }
                lastPeriodUidWithTracks = null
            }
        return false
    }

    private fun hasSelectedVideoTrack(player: Player): Boolean =
        player.isCommandAvailable(Player.COMMAND_GET_TRACKS) &&
                player.currentTracks.isTypeSelected(C.TRACK_TYPE_VIDEO)
}