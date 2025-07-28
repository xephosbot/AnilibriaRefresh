package com.xbot.player.ui

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

class AVPlayer : VideoPlayer {

    override val state: StateFlow<VideoPlayerState>
        get() = TODO("Not yet implemented")

    override fun play() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun setUrl(url: String) {
        TODO("Not yet implemented")
    }

    override fun setUrls(urls: List<String>) {
        TODO("Not yet implemented")
    }

    override fun playAt(index: Int) {
        TODO("Not yet implemented")
    }

    override fun playNext() {
        TODO("Not yet implemented")
    }

    override fun playPrevious() {
        TODO("Not yet implemented")
    }

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun seekTo(position: Duration) {
        TODO("Not yet implemented")
    }
}