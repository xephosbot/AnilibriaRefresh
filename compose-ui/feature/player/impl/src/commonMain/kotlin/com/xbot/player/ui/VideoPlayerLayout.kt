package com.xbot.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface

@Composable
fun VideoPlayerLayout(
    player: VideoPlayerState,
    controls: @Composable () -> Unit,
    surfaceScale: ContentScale = ContentScale.Fit,
    coverSurfaceColor: Color = Color.Black,
    modifier: Modifier = Modifier,
) {
    VideoPlayerSurface(
        playerState = player,
        modifier = modifier
            .background(coverSurfaceColor)
            .fillMaxSize(),
        contentScale = surfaceScale,
        overlay = controls
    )
}
