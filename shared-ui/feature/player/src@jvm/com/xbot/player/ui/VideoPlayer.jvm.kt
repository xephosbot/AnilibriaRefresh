package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
actual fun rememberVideoPlayer(player: VideoPlayer): VideoPlayer {
    DisposableEffect(Unit) {
        onDispose {
            player.stop()
        }
    }
    return remember { player }
}