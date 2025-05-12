package com.xbot.player.platform

import android.content.Context
import androidx.compose.ui.util.fastForEach
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.xbot.player.ui.PlaybackState
import com.xbot.player.ui.VideoPlayerController
import com.xbot.player.ui.VideoPlayerEvents
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.time.Duration

class ExoPlayerController(
    context: Context,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : VideoPlayerController {
    internal val exoPlayer = ExoPlayer.Builder(context).build()

    private val playerScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val listeners = mutableListOf<VideoPlayerEvents>()
    private val exoPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val playerState = when (playbackState) {
                Player.STATE_IDLE -> PlaybackState.IDLE
                Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                Player.STATE_READY -> PlaybackState.READY
                Player.STATE_ENDED -> PlaybackState.STOPPED
                else -> PlaybackState.IDLE
            }

            listeners.fastForEach { listener ->
                listener.onPlaybackStateChanged(playerState)
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val playerState = when (isPlaying) {
                true -> PlaybackState.PLAYING
                false -> PlaybackState.PAUSED
            }

            listeners.fastForEach { listener ->
                listener.onPlaybackStateChanged(playerState)
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            listeners.fastForEach { listener ->
                listener.onPlaybackStateChanged(PlaybackState.ERROR)
                listener.onPlaybackError(error)
            }
            error.printStackTrace()
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            listeners.fastForEach { listener ->
                listener.onVideoSizeChanged(videoSize.width, videoSize.height, videoSize.pixelWidthHeightRatio)
            }
        }

        override fun onRenderedFirstFrame() {
            listeners.fastForEach(VideoPlayerEvents::onRenderedFirstFrame)
        }
    }

    init {
        exoPlayer.addListener(exoPlayerListener)
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun setUrl(url: String) {
        setUrls(listOf(url))
    }

    override fun setUrls(urls: List<String>) {
        exoPlayer.clearMediaItems()
        exoPlayer.setMediaItems(urls.map(MediaItem::fromUri))

        if (urls.isNotEmpty()) {
            exoPlayer.prepare()
        }
    }

    override fun seekTo(position: Duration) {
        exoPlayer.seekTo(position.inWholeMilliseconds)
    }

    override fun release() {
        listeners.clear()
        exoPlayer.removeListener(exoPlayerListener)
        exoPlayer.release()
    }

    override fun addEventListener(listener: VideoPlayerEvents) {
        listeners.add(listener)
    }

    override fun removeEventListener(listener: VideoPlayerEvents) {
        listeners.remove(listener)
    }
}