package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW

@Composable
internal actual fun PlatformPlayerSurface(
    player: VideoPlayer,
    modifier: Modifier,
) {
    check(player is ExoPlayer) { "Player is not an instance of ExoPlayerController" }

    PlayerSurface(
        modifier = modifier,
        player = player.exoPlayer,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW
    )
}
