package com.xbot.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState

interface PictureInPictureController {
    val modifier: Modifier
    val isInPictureInPictureMode: Boolean
    fun enterPictureInPictureMode()
}

@Composable
expect fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController
