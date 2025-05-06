package com.xbot.player.platform

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.xbot.player.ui.VideoPlayerController
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class ExoPlayerController(context: Context) : VideoPlayerController {

    val exoPlayer: Player = ExoPlayer.Builder(context).build()

    override fun play() {
        Log.e("ExoPlayerController", "play")
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        Log.e("ExoPlayerController", "stop")
        exoPlayer.stop()
    }

    override fun setUrl(url: String) {
        Log.e("ExoPlayerController", "setUrl")
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.prepare()
    }

    override fun setUrls(urls: List<String>) {
        exoPlayer.setMediaItems(urls.map(MediaItem::fromUri))
        exoPlayer.prepare()
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun seekTo(position: Duration) {
        exoPlayer.seekTo(position.inWholeMilliseconds)
    }

    override fun currentPosition(): Duration {
        return exoPlayer.currentPosition.milliseconds
    }
}