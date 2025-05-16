package com.xbot.player.ui

import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.openani.mediamp.vlc.SkiaBitmapVideoSurface
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.media.TrackType
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import javax.swing.SwingUtilities
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class VLCPlayer(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : VideoPlayer {
    private val mediaPlayer: EmbeddedMediaPlayer = MediaPlayerFactory()
        .mediaPlayers()
        .newEmbeddedMediaPlayer()
    internal val surface: SkiaBitmapVideoSurface = SkiaBitmapVideoSurface().apply {
        mediaPlayer.videoSurface().set(this)
        attach(mediaPlayer)
    }

    private val playerScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val listeners = mutableListOf<VideoPlayerEvents>()
    private val mediaPlayerListener = object : MediaPlayerEventAdapter() {
        override fun playing(mediaPlayer: MediaPlayer) {
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.PLAYING)
                }
            }
        }

        override fun paused(mediaPlayer: MediaPlayer) {
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.PAUSED)
                }
            }
        }

        override fun stopped(mediaPlayer: MediaPlayer) {
            surface.setRenderingEnabled(false)
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.STOPPED)
                }
            }
        }

        override fun buffering(mediaPlayer: MediaPlayer, newCache: Float) {
            if (newCache < 1f) {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.BUFFERING)
                }
            } else {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.READY)
                }
            }
        }

        override fun finished(mediaPlayer: MediaPlayer) {
            surface.setRenderingEnabled(false)
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.STOPPED)
                }
            }
        }

        override fun timeChanged(mediaPlayer: MediaPlayer, newTime: Long) {
            listeners.fastForEach { listener ->
                listener.onCurrentPositionChanged(
                    newTime.milliseconds,
                    newTime.milliseconds
                )
            }
        }

        override fun lengthChanged(mediaPlayer: MediaPlayer, newLength: Long) {
            listeners.fastForEach { listener ->
                listener.onDurationChanged(newLength.milliseconds)
            }
        }

        override fun error(mediaPlayer: MediaPlayer) {
            surface.setRenderingEnabled(false)
            listeners.fastForEach { listener ->
                listener.onPlaybackStateChanged(PlaybackState.ERROR)
                listener.onPlaybackError(RuntimeException("VLC playback error"))
            }
        }

        override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
            surface.setRenderingEnabled(true)
            val videoTrack = mediaPlayer.media().info()?.videoTracks()?.firstOrNull()
            val width = videoTrack?.width() ?: 0
            val height = videoTrack?.height() ?: 0
            val pixelAspectRatio =
                videoTrack?.let { it.sampleAspectRatio().toFloat() / it.sampleAspectRatioBase() } ?: 1f
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onRenderedFirstFrame()
                    listener.onVideoSizeChanged(width, height, pixelAspectRatio)
                }
            }
        }

        override fun mediaChanged(mediaPlayer: MediaPlayer, media: MediaRef) {
            SwingUtilities.invokeLater {
                listeners.fastForEach { listener ->
                    listener.onPlaybackStateChanged(PlaybackState.READY)
                }
            }
        }

        override fun elementaryStreamSelected(mediaPlayer: MediaPlayer, type: TrackType, id: Int) {
            if (type == TrackType.VIDEO && id != -1) {
                val videoTrack = mediaPlayer.media().info()?.videoTracks()?.firstOrNull()
                val width = videoTrack?.width() ?: 0
                val height = videoTrack?.height() ?: 0
                val pixelAspectRatio =
                    videoTrack?.let { it.sampleAspectRatio().toFloat() / it.sampleAspectRatioBase() } ?: 1f
                listeners.fastForEach { listener ->
                    listener.onVideoSizeChanged(width, height, pixelAspectRatio)
                }
            }
        }
    }

    init {
        mediaPlayer.events().addMediaPlayerEventListener(mediaPlayerListener)
    }

    override fun play() {
        mediaPlayer.controls().setPause(false)
    }

    override fun pause() {
        mediaPlayer.controls().setPause(true)
    }

    override fun stop() {
        surface.setRenderingEnabled(false)
        surface.clearBitmap()
        mediaPlayer.controls().stop()
    }

    override fun setUrl(url: String) {
        setUrls(listOf(url))
    }

    override fun setUrls(urls: List<String>) {
        mediaPlayer.controls().stop()

        if (urls.isNotEmpty()) {
            mediaPlayer.media().prepare(urls.first())
            mediaPlayer.media().parsing().parse()
            mediaPlayer.controls().start()
        }
    }

    override fun seekTo(position: Duration) {
        mediaPlayer.controls().setTime(position.inWholeMilliseconds)
    }

    override fun release() {
        surface.setRenderingEnabled(false)
        surface.clearBitmap()
        listeners.clear()
        mediaPlayer.events().removeMediaPlayerEventListener(mediaPlayerListener)
        mediaPlayer.release()
    }

    override fun addEventListener(listener: VideoPlayerEvents) {
        listeners.add(listener)
    }

    override fun removeEventListener(listener: VideoPlayerEvents) {
        listeners.remove(listener)
    }
}
