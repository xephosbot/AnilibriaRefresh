package com.xbot.player.ui

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import kotlin.time.Duration

@Composable
expect fun rememberVideoPlayer(player: VideoPlayer = koinInject()): VideoPlayer

interface VideoPlayer {
    fun play()
    fun pause()
    fun stop()
    fun setUrl(url: String)
    fun setUrls(urls: List<String>)
    fun seekTo(position: Duration)
    fun release()
    fun addEventListener(listener: VideoPlayerEvents)
    fun removeEventListener(listener: VideoPlayerEvents)
}