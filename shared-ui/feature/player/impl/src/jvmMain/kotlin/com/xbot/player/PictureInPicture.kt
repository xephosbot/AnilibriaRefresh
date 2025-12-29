package com.xbot.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState

@Composable
actual fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController {
    return object : PictureInPictureController {
        override val isInPictureInPictureMode: Boolean = false
        override val modifier: Modifier = Modifier
    }
}
