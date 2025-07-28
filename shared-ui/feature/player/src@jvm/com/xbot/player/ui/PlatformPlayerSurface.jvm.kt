package com.xbot.player.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.toIntSize

@Composable
internal actual fun PlatformPlayerSurface(
    player: VideoPlayer,
    modifier: Modifier,
) {
    check(player is VLCPlayer) { "Player is not an instance of VLCPlayerController" }

    Canvas(modifier = modifier.fillMaxSize()) {
        val bitmap = player.surface?.bitmap ?: return@Canvas
        drawImage(
            bitmap,
            dstSize = size.toIntSize(),
            filterQuality = FilterQuality.High,
        )
    }
}