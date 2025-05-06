package com.xbot.player.ui

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import kotlin.time.Duration

@Composable
expect fun rememberVideoPlayerController(controller: VideoPlayerController = koinInject()): VideoPlayerController

interface VideoPlayerController {
    fun play()
    fun pause()
    fun stop()
    fun setUrl(url: String)
    fun setUrls(urls: List<String>)
    fun release()
    fun seekTo(position: Duration)
    fun currentPosition(): Duration
}