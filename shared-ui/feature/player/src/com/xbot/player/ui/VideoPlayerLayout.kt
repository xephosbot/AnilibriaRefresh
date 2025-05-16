package com.xbot.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.xbot.player.ui.modifier.resizeWithContentScale
import com.xbot.player.ui.state.rememberPresentationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

enum class ShowBuffering {
    /**
     * The buffering indicator is never shown.
     */
    NEVER,

    /**
     * The buffering indicator is always shown when the player is in the
     * [buffering][PlaybackState.BUFFERING] state.
     */
    ALWAYS,
}

@Composable
fun VideoPlayerLayout(
    player: VideoPlayer,
    controls: @Composable () -> Unit,
    buffering: @Composable () -> Unit,
    showBuffering: ShowBuffering = ShowBuffering.ALWAYS,
    surfaceScale: ContentScale = ContentScale.Fit,
    coverSurfaceColor: Color = Color.Black,
    modifier: Modifier = Modifier,
) {
    val presentationState = rememberPresentationState(player)
    val scope = rememberCoroutineScope()

    /* Just for testing */
    LaunchedEffect(presentationState) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                println(presentationState.videoSizeDp)
                delay(1.seconds)
            }
        }
    }

    Box(
        modifier = modifier.background(coverSurfaceColor),
        contentAlignment = Alignment.Center,
    ) {
        PlatformPlayerSurface(
            player = player,
            modifier = Modifier.resizeWithContentScale(
                contentScale = surfaceScale,
                sourceSizeDp = presentationState.videoSizeDp
            )
        )

        if (presentationState.coverSurface) {
            Box(Modifier.matchParentSize().background(coverSurfaceColor))
        }

        controls()
    }
}