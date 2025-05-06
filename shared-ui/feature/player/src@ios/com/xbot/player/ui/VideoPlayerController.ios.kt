package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
actual fun rememberVideoPlayerController(controller: VideoPlayerController): VideoPlayerController {
    DisposableEffect(Unit) {
        onDispose {
            controller.stop()
        }
    }
    return remember { controller }
}