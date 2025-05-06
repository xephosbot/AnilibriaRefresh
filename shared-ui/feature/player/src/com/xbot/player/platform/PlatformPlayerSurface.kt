package com.xbot.player.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xbot.player.ui.VideoPlayerController

@Composable
internal expect fun PlatformPlayerSurface(
    player: VideoPlayerController,
    modifier: Modifier = Modifier,
)