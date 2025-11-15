package com.xbot.player.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Pause
import com.xbot.designsystem.icons.PlayArrow
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerController(
    player: VideoPlayerState,
    buffering: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
) {
    var isControllerVisible by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isControllerVisible = !isControllerVisible
                    }
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        ControllerOverlay(
            isVisible = isControllerVisible,
            playerState = player,
            onClickBack = onClickBack,
            onPlayPause = {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            },
            onUserInteraction = { isControllerVisible = true }
        )

        if (player.isLoading) {
            buffering()
        }
    }
}

@Composable
private fun ControllerOverlay(
    isVisible: Boolean,
    playerState: VideoPlayerState,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onUserInteraction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = isVisible,
        modifier = modifier,
        label = "controller_visibility"
    ) { shouldShow ->
        if (shouldShow) {
            AutoHidingController(
                playerState = playerState,
                onClickBack = onClickBack,
                onPlayPause = onPlayPause,
                onUserInteraction = onUserInteraction
            )
        }
    }
}

@Composable
private fun AutoHidingController(
    playerState: VideoPlayerState,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onUserInteraction: () -> Unit,
) {
    var hideControllerKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(hideControllerKey) {
        delay(CONTROLLER_HIDE_DELAY_MS)
        onUserInteraction()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CONTROLLER_OVERLAY_COLOR),
        contentAlignment = Alignment.Center
    ) {
        VideoPlayerTopBar(
            title = "Какое-то аниме",
            onClickBack = onClickBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (!playerState.isLoading) {
            PlayPauseButton(
                state = playerState,
                onClick = {
                    onPlayPause()
                    hideControllerKey++
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoPlayerTopBar(
    title: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = AnilibriaIcons.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayPauseButton(
    state: VideoPlayerState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = if (state.isPlaying) {
        AnilibriaIcons.Filled.Pause
    } else {
        AnilibriaIcons.Filled.PlayArrow
    }

    FilledTonalIconToggleButton(
        modifier = modifier.size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        checked = state.isPlaying,
        onCheckedChange = { onClick() },
        shapes = IconButtonDefaults.toggleableShapes()
    ) {
        Icon(
            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
            imageVector = icon,
            contentDescription = null,
        )
    }
}

private const val CONTROLLER_HIDE_DELAY_MS = 3000L
private val CONTROLLER_OVERLAY_COLOR = Color.Black.copy(alpha = 0.6f)