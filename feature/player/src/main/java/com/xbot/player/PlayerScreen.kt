package com.xbot.player

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.media3.common.Player
import com.xbot.player.ui.Media
import com.xbot.player.ui.PlayerController
import com.xbot.player.ui.ShowBuffering
import com.xbot.player.ui.rememberMediaState

@Composable
fun PlayerScreen(
    player: Player?,
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onToggleFullScreen: () -> Unit
) {
    val mediaState = rememberMediaState(player)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Media(
                state = mediaState,
                modifier = Modifier.fillMaxSize(),
                showBuffering = ShowBuffering.Always,
                buffering = {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = LocalContentColor.current)
                    }
                },
                controller = { state, isBuffering ->
                    PlayerController(
                        mediaState = state,
                        isBuffering = isBuffering,
                        navigationIcon = {
                            IconButton(onClick = onCloseClick) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                )
                            }
                        },
                        bottomActions = {
                            IconButton(onClick = onSettingsClick) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                )
                            }
                            IconButton(onClick = onToggleFullScreen) {
                                Icon(
                                    imageVector = when (isLandscape) {
                                        true -> Icons.Default.FullscreenExit
                                        else -> Icons.Default.Fullscreen
                                    },
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                }
            )
        }
    }
}
