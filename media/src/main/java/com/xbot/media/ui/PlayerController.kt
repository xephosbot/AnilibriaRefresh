package com.xbot.media.ui

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.delay

/**
 * Основной контроллер проигрывателя.
 *
 * @param modifier Модификатор для внешнего вида и положения.
 * @param mediaState Состояние медиа, в том числе данные о текущем треке.
 * @param isBuffering Указывает, происходит ли буферизация.
 * @param navigationIcon Иконка для навигации, по умолчанию пустой контент.
 * @param bottomActions Действия, которые отображаются в нижней части контроллера.
 * @param windowInsets Вставки окна для учета вырезов и системных жестов.
 */
@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    mediaState: MediaState,
    isBuffering: Boolean,
    navigationIcon: @Composable () -> Unit = {},
    bottomActions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = WindowInsets.displayCutout
) {
    Crossfade(targetState = mediaState.isControllerShowing, modifier = modifier) { isShowing ->
        if (isShowing) {
            val controllerState = rememberControllerState(mediaState)
            var dragging by remember { mutableStateOf(false) }
            val hideOnTimeout = !mediaState.shouldShowControllerIndefinitely && !dragging
            var hideEffectTrigger by remember { mutableIntStateOf(0) }

            LaunchedEffect(hideOnTimeout, hideEffectTrigger) {
                if (hideOnTimeout) {
                    delay(3000)
                    mediaState.isControllerShowing = false
                }
            }

            LaunchedEffect(Unit) {
                while (true) {
                    controllerState.triggerPositionUpdate()
                    delay(200)
                }
            }

            ControllerLayout(
                modifier = Modifier.background(BackgroundOverlayColor),
                mediaState = mediaState,
                controllerState = controllerState,
                isBuffering = isBuffering,
                navigationIcon = navigationIcon,
                bottomActions = bottomActions,
                windowInsets = windowInsets,
                onPositionChangeStart = {
                    dragging = true
                },
                onPositionChangeStop = { position ->
                    controllerState.seekTo(position)
                    dragging = false
                },
                onPlayPauseClicked = {
                    controllerState.playOrPause()
                    hideEffectTrigger++
                }
            )
        }
    }
}

@Composable
private fun ControllerLayout(
    modifier: Modifier,
    mediaState: MediaState,
    controllerState: ControllerState,
    isBuffering: Boolean,
    navigationIcon: @Composable () -> Unit,
    bottomActions: @Composable RowScope.() -> Unit,
    windowInsets: WindowInsets,
    onPositionChangeStart: (Long) -> Unit,
    onPositionChangeStop: (Long) -> Unit,
    onPlayPauseClicked: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        PlayerControllerTopBar(
            modifier = Modifier.align(Alignment.TopCenter),
            contentPadding = windowInsets.getHorizontalPadding(),
            mediaInfo = mediaState.playerState?.mediaMetadata.toMediaInfo(),
            navigationIcon = navigationIcon
        )

        if (!isBuffering) {
            PlayPauseIcon(
                modifier = Modifier.align(Alignment.Center),
                isPlaying = controllerState.showPause,
                onClick = onPlayPauseClicked
            )
        }

        PlayerControllerBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            controllerState = controllerState,
            onPositionChangeStart = onPositionChangeStart,
            onPositionChangeStop = onPositionChangeStop,
            bottomActions = bottomActions,
            windowInsets = windowInsets.getBottomPadding()
        )
    }
}

@Composable
private fun PlayPauseIcon(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource? = null
) {
    val icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
    Icon(
        modifier = modifier
            .size(52.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false),
                onClick = onClick
            ),
        imageVector = icon,
        contentDescription = null
    )
}

@Composable
private fun PlayerControllerBottomBar(
    modifier: Modifier = Modifier,
    controllerState: ControllerState,
    onPositionChangeStart: (Long) -> Unit,
    onPositionChangeStop: (Long) -> Unit,
    bottomActions: @Composable RowScope.() -> Unit,
    windowInsets: PaddingValues
) {
    val durationText by remember { derivedStateOf { controllerState.durationMs.toTime() } }
    val positionText by remember { derivedStateOf { controllerState.positionMs.toTime() } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(windowInsets)
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$positionText/$durationText",
                fontSize = 14.sp
            )
            Spacer(Modifier.weight(1f))
            bottomActions()
        }
        TimeBar(
            durationMs = controllerState.durationMs,
            positionMs = controllerState.positionMs,
            bufferedPositionMs = controllerState.bufferedPositionMs,
            onPositionChangeStart = onPositionChangeStart,
            onPositionChangeStop = onPositionChangeStop
        )
    }
}

/**
 * Верхняя панель контроллера, отображающая название и подзаголовок медиа.
 *
 * @param modifier Модификатор внешнего вида и выравнивания.
 * @param contentPadding Внешние отступы панели.
 * @param mediaInfo Информация о текущем медиа.
 * @param navigationIcon Иконка навигации.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerControllerTopBar(
    modifier: Modifier,
    contentPadding: PaddingValues,
    mediaInfo: MediaInfo,
    navigationIcon: @Composable () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier.padding(horizontal = contentPadding.calculateMaxPadding()),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = mediaInfo.title, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = mediaInfo.subtitle, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = LocalContentColor.current,
            navigationIconContentColor = LocalContentColor.current
        )
    )
}

/**
 * Класс для хранения информации о текущем медиа.
 */
private data class MediaInfo(
    val title: String,
    val subtitle: String
)

private fun MediaMetadata?.toMediaInfo(): MediaInfo = MediaInfo(
    title = this?.title?.toString().orEmpty(),
    subtitle = this?.artist?.toString().orEmpty()
)

@SuppressLint("DefaultLocale")
private fun Long.toTime(): String {
    if (this < 0) return "--:--"

    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

// Simplifies horizontal padding calculations for insets.
@Composable
private fun WindowInsets.getHorizontalPadding(): PaddingValues =
    this.only(WindowInsetsSides.Horizontal).asPaddingValues()

// Simplifies bottom padding calculation, taking system gestures into account.
@Composable
private fun WindowInsets.getBottomPadding(): PaddingValues =
    this.union(WindowInsets.systemGestures.only(WindowInsetsSides.Bottom))
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        .asPaddingValues()

// Calculates the max padding for proper centering.
@Composable
private fun PaddingValues.calculateMaxPadding(
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
) = max(calculateLeftPadding(layoutDirection), calculateRightPadding(layoutDirection))

private val BackgroundOverlayColor = Color(0x98000000)