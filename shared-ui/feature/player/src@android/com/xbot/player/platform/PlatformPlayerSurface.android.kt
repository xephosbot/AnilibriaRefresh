package com.xbot.player.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import com.xbot.player.ui.VideoPlayerController

@Composable
internal actual fun PlatformPlayerSurface(
    player: VideoPlayerController,
    modifier: Modifier,
) {
    check(player is ExoPlayerController) { "Player is not an instance of ExoPlayerController" }

    PlayerSurface(
        modifier = modifier,
        player = player.exoPlayer,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW
    )
}
