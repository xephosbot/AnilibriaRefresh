package com.xbot.player.platform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.toIntSize
import com.xbot.player.ui.VideoPlayerController

@Composable
internal actual fun PlatformPlayerSurface(
    player: VideoPlayerController,
    modifier: Modifier,
) {
    check(player is VLCPlayerController) { "Player is not an instance of VLCPlayerController" }

    Canvas(modifier = modifier.fillMaxSize()) {
        val bitmap = player.surface.bitmap ?: return@Canvas
        drawImage(
            bitmap,
            dstSize = size.toIntSize(),
            filterQuality = FilterQuality.High,
        )
    }
}