package com.xbot.player.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration

class ExoPlayer(
    private val context: Context,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : VideoPlayer {
    internal var exoPlayer: Player? by mutableStateOf(null)
        private set

    private val playerScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val _state = MutableStateFlow(VideoPlayerState.EMPTY)

    private val exoPlayerListener = ExoPlayerListener()

    override val state: StateFlow<VideoPlayerState> = _state.asStateFlow()

    override fun play() = executePlayerAction { play() }

    override fun pause() = executePlayerAction { pause() }

    override fun stop() = executePlayerAction { stop() }

    override fun setUrl(url: String) {
        require(url.isNotBlank()) { "URL cannot be blank" }
        setUrls(listOf(url))
    }

    override fun setUrls(urls: List<String>) {
        require(urls.isNotEmpty()) { "URLs list cannot be empty" }
        require(urls.all { it.isNotBlank() }) { "All URLs must be non-blank" }

        updateState {
            copy(
                playlist = urls,
                currentIndex = 0
            )
        }

        executePlayerAction {
            clearMediaItems()
            setMediaItems(urls.map(MediaItem::fromUri))
            prepare()
            playWhenReady = true
        }
    }

    override fun playAt(index: Int) {
        val currentState = _state.value
        require(index >= 0 && index < currentState.playlist.size) {
            "Index $index is out of bounds for playlist of size ${currentState.playlist.size}"
        }

        updateState { copy(currentIndex = index) }

        executePlayerAction {
            seekToDefaultPosition(index)
            play()
        }
    }

    override fun playNext() {
        val currentState = _state.value
        if (currentState.hasNext) {
            playAt(currentState.currentIndex + 1)
        }
    }

    override fun playPrevious() {
        val currentState = _state.value
        if (currentState.hasPrevious) {
            playAt(currentState.currentIndex - 1)
        }
    }

    override fun seekTo(position: Duration) {
        require(position >= Duration.ZERO) { "Position must be non-negative" }
        executePlayerAction {
            seekTo(position.inWholeMilliseconds)
        }
    }

    override fun init() {
        check(exoPlayer == null) { "Player already initialized" }

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(exoPlayerListener)
        }
    }

    override fun release() {
        exoPlayer?.let { player ->
            stop()
            player.removeListener(exoPlayerListener)
            player.release()
        }
        exoPlayer = null

        // Reset state
        updateState { VideoPlayerState.EMPTY }
    }

    private fun updateState(transform: VideoPlayerState.() -> VideoPlayerState) {
        _state.update(transform)
    }

    private fun executePlayerAction(action: Player.() -> Unit) {
        exoPlayer?.action()
    }

    private inner class ExoPlayerListener : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            val newState = when (playbackState) {
                Player.STATE_IDLE -> VideoPlayerState.PlaybackState.Idle
                Player.STATE_READY -> VideoPlayerState.PlaybackState.Ready
                Player.STATE_BUFFERING -> VideoPlayerState.PlaybackState.Buffering
                Player.STATE_ENDED -> VideoPlayerState.PlaybackState.Stopped
                else -> VideoPlayerState.PlaybackState.Idle
            }

            updateState { copy(playbackState = newState) }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val newState = if (isPlaying) {
                VideoPlayerState.PlaybackState.Playing
            } else {
                VideoPlayerState.PlaybackState.Paused
            }

            updateState { copy(playbackState = newState) }
        }

        override fun onPlayerError(error: PlaybackException) {
            updateState {
                copy(playbackState = VideoPlayerState.PlaybackState.Error(error))
            }
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            val correctedSize = calculateCorrectedVideoSize(videoSize)
            updateState { copy(videoSize = correctedSize) }
        }

        override fun onRenderedFirstFrame() {
            updateState { copy(coverSurface = false) }
        }

        private fun calculateCorrectedVideoSize(videoSize: VideoSize): Size? {
            val width = videoSize.width.toFloat()
            val height = videoSize.height.toFloat()

            if (width <= 0f || height <= 0f) return null

            val aspectRatio = videoSize.pixelWidthHeightRatio

            return when {
                aspectRatio < 1.0f -> Size(width * aspectRatio, height)
                aspectRatio > 1.0f -> Size(width, height / aspectRatio)
                else -> Size(width, height)
            }
        }
    }
}