package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformPlayerSurface(
    player: VideoPlayer,
    modifier: Modifier,
) {
    check(player is AVPlayer) { "Player is not an instance of AVPlayerController" }
}