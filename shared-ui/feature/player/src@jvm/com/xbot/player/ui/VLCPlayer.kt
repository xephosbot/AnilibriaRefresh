package com.xbot.player.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.openani.mediamp.vlc.SkiaBitmapVideoSurface
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class VLCPlayer(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : VideoPlayer {
    private var mediaPlayer: EmbeddedMediaPlayer? = null
    internal var surface: SkiaBitmapVideoSurface? by mutableStateOf(null)
        private set

    private val playerScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val _state = MutableStateFlow(VideoPlayerState.EMPTY)

    private val mediaPlayerListener = VLCPlayerEventListener()

    override val state: StateFlow<VideoPlayerState> = _state.asStateFlow()

    override fun play() = executePlayerAction {
        controls().setPause(false)
        println("VLCPlayer play")
    }

    override fun pause() = executePlayerAction {
        controls().setPause(true)
        println("VLCPlayer pause")
    }

    override fun stop() = executePlayerAction {
        surface?.apply {
            setRenderingEnabled(false)
            clearBitmap()
        }
        controls().stop()
    }

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

        playCurrentItem()
    }

    override fun playAt(index: Int) {
        val currentState = _state.value
        require(index >= 0 && index < currentState.playlist.size) {
            "Index $index is out of bounds for playlist of size ${currentState.playlist.size}"
        }

        updateState { copy(currentIndex = index) }
        playCurrentItem()
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
            controls().setTime(position.inWholeMilliseconds)
        }
    }

    override fun init() {
        check(mediaPlayer == null) { "Player already initialized" }

        val factory = MediaPlayerFactory(*VLC_ARGS)
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer().apply {
            events().addMediaPlayerEventListener(mediaPlayerListener)
        }

        surface = SkiaBitmapVideoSurface().apply {
            mediaPlayer?.videoSurface()?.set(this)
            attach(mediaPlayer!!)
        }
    }

    override fun release() {
        mediaPlayer?.let { player ->
            stop()
            player.events().removeMediaPlayerEventListener(mediaPlayerListener)
            player.release()
        }

        surface = null
        mediaPlayer = null

        // Reset state
        updateState { VideoPlayerState.EMPTY }
    }

    private fun playCurrentItem() {
        val currentState = _state.value
        val currentUrl = currentState.currentUrl

        if (currentUrl != null) {
            executePlayerAction {
                controls().stop()
                media().prepare(currentUrl)
                media().parsing().parse()
                controls().start()
            }
        }
    }

    private fun updateState(transform: VideoPlayerState.() -> VideoPlayerState) {
        _state.update(transform)
    }

    private fun executePlayerAction(action: MediaPlayer.() -> Unit) {
        mediaPlayer?.submit { mediaPlayer?.action() }
    }

    private inner class VLCPlayerEventListener : MediaPlayerEventAdapter() {

        override fun playing(mediaPlayer: MediaPlayer) {
            updateState {
                copy(playbackState = VideoPlayerState.PlaybackState.Playing)
            }
        }

        override fun paused(mediaPlayer: MediaPlayer) {
            updateState {
                copy(playbackState = VideoPlayerState.PlaybackState.Paused)
            }
        }

        override fun stopped(mediaPlayer: MediaPlayer) {
            surface?.setRenderingEnabled(false)
            updateState {
                copy(playbackState = VideoPlayerState.PlaybackState.Stopped)
            }
        }

        override fun buffering(mediaPlayer: MediaPlayer, newCache: Float) {
            val playbackState = when {
                newCache < BUFFERING_THRESHOLD -> VideoPlayerState.PlaybackState.Buffering
                mediaPlayer.status().isPlaying -> VideoPlayerState.PlaybackState.Playing
                else -> VideoPlayerState.PlaybackState.Ready
            }

            updateState { copy(playbackState = playbackState) }
        }

        override fun finished(mediaPlayer: MediaPlayer) {
            surface?.setRenderingEnabled(false)

            // Auto-play next item if available
            val currentState = _state.value
            if (currentState.hasNext) {
                playNext()
            } else {
                updateState {
                    copy(playbackState = VideoPlayerState.PlaybackState.Stopped)
                }
            }
        }

        override fun timeChanged(mediaPlayer: MediaPlayer, newTime: Long) {
            val timePosition = newTime.milliseconds
            updateState {
                copy(
                    currentPosition = timePosition,
                    bufferedPosition = timePosition,
                )
            }
        }

        override fun lengthChanged(mediaPlayer: MediaPlayer, newLength: Long) {
            updateState {
                copy(duration = newLength.milliseconds)
            }
        }

        override fun error(mediaPlayer: MediaPlayer) {
            surface?.setRenderingEnabled(false)
            updateState {
                copy(
                    playbackState = VideoPlayerState.PlaybackState.Error(
                        RuntimeException("VLC playback error")
                    )
                )
            }
        }

        override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
            surface?.setRenderingEnabled(true)

            val videoSize = calculateVideoSize(mediaPlayer)
            updateState {
                copy(
                    videoSize = videoSize,
                    coverSurface = false
                )
            }
        }

        override fun mediaChanged(mediaPlayer: MediaPlayer, media: MediaRef) {
            updateState {
                copy(playbackState = VideoPlayerState.PlaybackState.Ready)
            }
        }

        private fun calculateVideoSize(mediaPlayer: MediaPlayer): Size? {
            val videoTrack = mediaPlayer.media().info()?.videoTracks()?.firstOrNull()
                ?: return null

            val width = videoTrack.width().toFloat()
            val height = videoTrack.height().toFloat()

            if (width <= 0f || height <= 0f) return null

            val aspectRatio = videoTrack.sampleAspectRatio().toFloat() /
                    videoTrack.sampleAspectRatioBase()

            return when {
                aspectRatio < 1.0f -> Size(width * aspectRatio, height)
                aspectRatio > 1.0f -> Size(width, height / aspectRatio)
                else -> Size(width, height)
            }
        }
    }

    companion object {
        private const val BUFFERING_THRESHOLD = 100f
        private val VLC_ARGS = arrayOf(
            "--network-caching=3000",
            "--file-caching=1000",
            "--live-caching=1500",
            "--sout-mux-caching=500",
            "--ts-seek-percent",
            "--no-ts-cc-check",
            "--no-ts-trust-pcr",
            "--avcodec-hw=any",
            "--avcodec-threads=0",
            "--clock-synchro=0",
            "--audio-desync=0",
        )
    }
}
