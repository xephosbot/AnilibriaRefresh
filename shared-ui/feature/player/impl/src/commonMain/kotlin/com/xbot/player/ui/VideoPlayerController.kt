package com.xbot.player.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.ArrowBack
import com.xbot.designsystem.icons.Pause
import com.xbot.designsystem.icons.PlayArrow
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import kotlinx.coroutines.delay
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerController(
    player: VideoPlayerState,
    title: String,
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
            title = title,
            onClickBack = onClickBack,
            onPlayPause = {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            },
            onTimeout = { isControllerVisible = false }
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
    title: String,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onTimeout: () -> Unit,
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
                title = title,
                onClickBack = onClickBack,
                onPlayPause = onPlayPause,
                onTimeout = onTimeout
            )
        }
    }
}

@Composable
private fun AutoHidingController(
    playerState: VideoPlayerState,
    title: String,
    onClickBack: () -> Unit,
    onPlayPause: () -> Unit,
    onTimeout: () -> Unit,
) {
    var hideControllerKey by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(hideControllerKey, playerState.isPlaying, playerState.userDragging) {
        if (playerState.isPlaying && !playerState.userDragging) {
            delay(CONTROLLER_HIDE_DELAY_MS)
            onTimeout()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CONTROLLER_OVERLAY_COLOR)
            .symmetricInsetsPadding(),
        contentAlignment = Alignment.Center
    ) {
        VideoPlayerTopBar(
            title = title,
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

        TimelineControls(
            playerState = playerState,
            onInteraction = { hideControllerKey++ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TimelineControls(
    playerState: VideoPlayerState,
    onInteraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Slider(
            value = playerState.sliderPos,
            onValueChange = {
                playerState.sliderPos = it
                playerState.userDragging = true
                onInteraction()
            },
            onValueChangeFinished = {
                playerState.userDragging = false
                playerState.seekTo(playerState.sliderPos)
                onInteraction()
            },
            valueRange = 0f..1000f,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = playerState.positionText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = playerState.durationText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
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
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        windowInsets = WindowInsets(0)
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

@Composable
private fun Modifier.symmetricInsetsPadding(): Modifier {
    val insets = WindowInsets.systemBars.union(WindowInsets.displayCutout)
    val symmetricInsets = remember(insets) {
        object : WindowInsets {
            override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int {
                val left = insets.getLeft(density, layoutDirection)
                val right = insets.getRight(density, layoutDirection)
                return max(left, right)
            }

            override fun getTop(density: Density): Int {
                return insets.getTop(density)
            }

            override fun getRight(density: Density, layoutDirection: LayoutDirection): Int {
                val left = insets.getLeft(density, layoutDirection)
                val right = insets.getRight(density, layoutDirection)
                return max(left, right)
            }

            override fun getBottom(density: Density): Int {
                return insets.getBottom(density)
            }
        }
    }
    return this.windowInsetsPadding(symmetricInsets)
        .consumeWindowInsets(symmetricInsets)
}

private const val CONTROLLER_HIDE_DELAY_MS = 3000L
private val CONTROLLER_OVERLAY_COLOR = Color.Black.copy(alpha = 0.6f)
