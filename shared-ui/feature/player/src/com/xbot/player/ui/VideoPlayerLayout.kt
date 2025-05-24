package com.xbot.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.xbot.player.ui.modifier.resizeWithContentScale

@Composable
fun VideoPlayerLayout(
    player: VideoPlayer,
    controls: @Composable () -> Unit,
    surfaceScale: ContentScale = ContentScale.Fit,
    coverSurfaceColor: Color = Color.Black,
    modifier: Modifier = Modifier,
) {
    val playerState by player.state.collectAsState()

    Box(
        modifier = modifier.background(coverSurfaceColor),
        contentAlignment = Alignment.Center,
    ) {
        PlatformPlayerSurface(
            player = player,
            modifier = Modifier.resizeWithContentScale(
                contentScale = surfaceScale,
                sourceSizeDp = playerState.videoSize
            )
        )

        if (playerState.coverSurface) {
            Box(Modifier.matchParentSize().background(coverSurfaceColor))
        }

        controls()
    }
}
