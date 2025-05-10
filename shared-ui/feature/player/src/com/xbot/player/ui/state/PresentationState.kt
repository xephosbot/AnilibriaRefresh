package com.xbot.player.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import com.xbot.player.ui.VideoPlayerController

@Composable
expect fun rememberPresentationState(
    controller: VideoPlayerController,
    keepContentOnReset: Boolean = false,
): PresentationState

interface PresentationState {
    val videoSizeDp: Size?
    val coverSurface: Boolean
    var keepContentOnReset: Boolean
}