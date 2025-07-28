package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.koinInject
import kotlin.time.Duration

@Composable
expect fun rememberVideoPlayer(player: VideoPlayer = koinInject()): VideoPlayer

interface VideoPlayer {
    val state: StateFlow<VideoPlayerState>

    // Basic playback controls
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Duration)
    fun init()
    fun release()

    // Playlist controls
    fun setUrl(url: String)
    fun setUrls(urls: List<String>)
    fun playAt(index: Int)
    fun playNext()
    fun playPrevious()
}

@Stable
data class VideoPlayerState(
    val playbackState: PlaybackState = PlaybackState.Buffering,
    val coverSurface: Boolean = true,
    val videoSize: Size? = null,
    val duration: Duration = Duration.ZERO,
    val currentPosition: Duration = Duration.ZERO,
    val bufferedPosition: Duration = Duration.ZERO,
    val playlist: List<String> = emptyList(),
    val currentIndex: Int = 0,
) {
    val isPlaying: Boolean
        get() = playbackState == PlaybackState.Playing

    val isBuffering: Boolean
        get() = playbackState == PlaybackState.Buffering

    val isPaused: Boolean
        get() = playbackState == PlaybackState.Paused

    val isReady: Boolean
        get() = playbackState == PlaybackState.Ready

    val hasError: Boolean
        get() = playbackState is PlaybackState.Error

    val hasPlaylist: Boolean
        get() = playlist.isNotEmpty()

    val currentUrl: String?
        get() = playlist.getOrNull(currentIndex)

    val hasNext: Boolean
        get() = currentIndex < playlist.size - 1

    val hasPrevious: Boolean
        get() = currentIndex > 0

    @Stable
    sealed interface PlaybackState {
        @Stable
        data object Idle : PlaybackState

        @Stable
        data object Ready : PlaybackState

        @Stable
        data object Buffering : PlaybackState

        @Stable
        data object Playing : PlaybackState

        @Stable
        data object Paused : PlaybackState

        @Stable
        data object Stopped : PlaybackState

        @Stable
        data class Error(val error: Throwable) : PlaybackState
    }

    companion object {
        val EMPTY = VideoPlayerState()
    }
}