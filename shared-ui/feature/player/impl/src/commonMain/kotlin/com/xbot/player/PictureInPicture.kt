package com.xbot.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState

interface PictureInPictureController {
    val isInPictureInPictureMode: Boolean
    val modifier: Modifier
}

@Composable
expect fun rememberPictureInPictureController(player: VideoPlayerState): PictureInPictureController
