package com.xbot.player.platform

import com.xbot.player.ui.VideoPlayerController
import org.openani.mediamp.vlc.SkiaBitmapVideoSurface
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import java.awt.Component
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class VLCPlayerController : VideoPlayerController {

    val component: Component = initializeMediaPlayerComponent()

    val player: EmbeddedMediaPlayer = component.mediaPlayer()

    val surface: SkiaBitmapVideoSurface = SkiaBitmapVideoSurface().apply {
        player.videoSurface().set(this)
        attach(player)
    }

    init {
        player.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
            override fun playing(mediaPlayer: MediaPlayer?) {
                surface.enableRendering.value = true
            }

            override fun stopped(mediaPlayer: MediaPlayer?) {
                surface.enableRendering.value = false
            }

            override fun paused(mediaPlayer: MediaPlayer) {
                surface.enableRendering.value = false
            }

            override fun finished(mediaPlayer: MediaPlayer) {
                surface.enableRendering.value = false
            }

            override fun error(mediaPlayer: MediaPlayer) {
                surface.enableRendering.value = false
            }
        })
    }

    private fun initializeMediaPlayerComponent(): Component {
        NativeDiscovery().discover()
        return if (isMacOS()) {
            CallbackMediaPlayerComponent()
        } else {
            EmbeddedMediaPlayerComponent()
        }
    }

    override fun play() {
        player.controls()?.start()
    }

    override fun pause() {
        player.controls()?.pause()
    }

    override fun stop() {
        player.submit {
            player.controls().stop()
        }
        surface.clearBitmap()
        surface.enableRendering.value = false
    }

    override fun setUrl(url: String) {
        player.media()?.play(url)
    }

    override fun setUrls(urls: List<String>) {
        //TODO("Not yet implemented")
    }

    override fun release() {
        player.release()
    }

    override fun seekTo(position: Duration) {
        player.controls()?.setTime(position.inWholeSeconds)
    }

    override fun currentPosition(): Duration {
        //TODO("Not yet implemented")
        return 0.seconds
    }

    private fun Component.mediaPlayer() = when (this) {
        is CallbackMediaPlayerComponent -> mediaPlayer()
        is EmbeddedMediaPlayerComponent -> mediaPlayer()
        else -> error("mediaPlayer() can only be called on vlcj player components")
    }

    private fun isMacOS(): Boolean {
        val os = System
            .getProperty("os.name", "generic")
            .lowercase(Locale.ENGLISH)
        return "mac" in os || "darwin" in os
    }
}
