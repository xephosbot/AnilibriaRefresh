package com.xbot.player.ui

import kotlin.time.Duration

interface VideoPlayerEvents {
    fun onPlaybackStateChanged(state: PlaybackState) = Unit
    fun onPlaybackError(error: Throwable) = Unit
    fun onRenderedFirstFrame() = Unit
    fun onVideoSizeChanged(width: Int, height: Int, pixelAspectRatio: Float) = Unit
    fun onDurationChanged(duration: Duration) = Unit
    fun onCurrentPositionChanged(currentPosition: Duration, bufferedPosition: Duration) = Unit
}

enum class PlaybackState {
    IDLE,
    BUFFERING,
    READY,
    PLAYING,
    PAUSED,
    STOPPED,
    ERROR
}