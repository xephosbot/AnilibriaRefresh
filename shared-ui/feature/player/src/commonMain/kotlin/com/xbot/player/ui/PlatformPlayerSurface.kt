package com.xbot.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun PlatformPlayerSurface(
    player: VideoPlayer,
    modifier: Modifier = Modifier,
)