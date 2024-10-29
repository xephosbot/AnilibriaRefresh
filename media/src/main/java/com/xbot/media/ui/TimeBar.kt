package com.xbot.media.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
internal fun TimeBar(
    durationMs: Long,
    positionMs: Long,
    bufferedPositionMs: Long,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onPositionChangeStart: ((positionMs: Long) -> Unit)? = null,
    onPositionChange: ((positionMs: Long) -> Unit)? = null,
    onPositionChangeStop: ((positionMs: Long) -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (enable: Boolean, dragging: Boolean) -> Unit = { enable, dragging ->
        Thumb(enable, dragging)
    },
    track: @Composable (current: Float, dragged: Float, buffered: Float) -> Unit = { _, dragged, buffered ->
        Track(played = dragged, buffered = buffered)
    }
) {
    val currentPosition by rememberUpdatedState(positionMs)

    var isDragging by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableLongStateOf(0L) }
    if (!isDragging) dragPosition = positionMs
    val thumbPosition by remember {
        derivedStateOf { if (isDragging) dragPosition else currentPosition }
    }
    val positionFraction: Float by remember(durationMs) {
        derivedStateOf { if (durationMs != 0L) thumbPosition.toFloat() / durationMs else 0f }
    }

    var trackWidth by remember { mutableIntStateOf(0) }
    var thumbSize by remember { mutableIntStateOf(0) }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val drag = Modifier.draggable(
        orientation = Orientation.Horizontal,
        reverseDirection = isRtl,
        enabled = enabled,
        interactionSource = interactionSource,
        state = rememberDraggableState { delta ->
            val targetProgress = ((positionFraction * trackWidth + delta) / trackWidth)
                .coerceIn(0f, 1f)
            dragPosition = (targetProgress * durationMs).roundToLong()
            onPositionChange?.invoke(dragPosition)
        },
        onDragStarted = { startPosition ->
            isDragging = true
            val startX = startPosition.x - thumbSize
            dragPosition =
                (startX / trackWidth * durationMs).roundToLong().coerceIn(0L, durationMs)
            onPositionChangeStart?.invoke(dragPosition)
        },
        onDragStopped = {
            onPositionChangeStop?.invoke(dragPosition)
            isDragging = false
        },
        startDragImmediately = isDragging,
    )

    val thumbBox = @Composable {
        Box(
            modifier = Modifier.wrapContentWidth().onSizeChanged {
                thumbSize = it.width
            }
        ) {
            thumb(enabled, isDragging)
        }
    }
    val trackBox = @Composable {
        Box(
            modifier = Modifier.wrapContentWidth().onSizeChanged {
                trackWidth = it.width
            }
        ) {
            track(
                if (durationMs > 0) currentPosition.toFloat() / durationMs else 0f,
                if (durationMs > 0) thumbPosition.toFloat() / durationMs else 0f,
                if (durationMs > 0) bufferedPositionMs.toFloat() / durationMs else 0f
            )
        }
    }

    Layout(
        modifier = modifier
            .requiredSizeIn(
                minWidth = ThumbDefaultSize, minHeight = ThumbDefaultSize
            )
            .then(drag),
        contents = listOf(thumbBox, trackBox)
    ) { (thumbMeasurable, trackMeasurable), constraints ->
        val thumbPlaceable = thumbMeasurable.first()
            .measure(constraints)
        val trackPlaceable = trackMeasurable.first()
            .measure(constraints.copy(minHeight = 0))

        val sliderWidth = constraints.maxWidth
        val sliderHeight = ThumbDraggingSize.roundToPx()

        val trackOffsetX = 0
        val thumbOffsetX = (positionFraction * sliderWidth).roundToInt()
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2
        val thumbOffsetY = (sliderHeight - thumbPlaceable.height) / 2

        layout(width = sliderWidth, height = sliderHeight) {
            trackPlaceable.place(trackOffsetX, trackOffsetY)
            thumbPlaceable.place(thumbOffsetX, thumbOffsetY)
        }
    }
}

@Composable
internal fun Track(
    played: Float,
    buffered: Float,
    modifier: Modifier = Modifier,
    playedColor: Color = Color(0xFFFFFFFF),
    bufferedColor: Color = Color(0xCCFFFFFF),
    backgroundColor: Color = Color(0x33FFFFFF),
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(TrackHeight)
            .rotate(if (LocalLayoutDirection.current == LayoutDirection.Rtl) 180f else 0f)
    ) {
        val width = size.width
        var left = 0f
        // draw played
        if (played > 0) {
            val playedRight = played * width
            val playedWidth = playedRight - left
            drawRect(
                playedColor,
                topLeft = Offset(left, 0f),
                size = size.copy(width = playedWidth)
            )
            left = playedRight
        }
        // draw buffered
        if (buffered > played) {
            val bufferedRight = buffered * width
            val bufferedWidth = bufferedRight - left
            drawRect(
                bufferedColor,
                topLeft = Offset(left, 0f),
                size = size.copy(width = bufferedWidth)
            )
            left = bufferedRight
        }
        // draw unplayed
        if (left < size.width) {
            drawRect(
                backgroundColor,
                topLeft = Offset(left, 0f),
                size = size.copy(width = size.width - left)
            )
        }
    }
}

@Composable
internal fun Thumb(
    enabled: Boolean,
    dragging: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFFFFF),
    shape: Shape = CircleShape
) {
    val size = when {
        !enabled -> ThumbDisabledSize
        dragging -> ThumbDraggingSize
        else -> ThumbDefaultSize
    }
    Spacer(
        modifier = modifier
            .size(size)
            .background(color, shape)
    )
}

private val TrackHeight = 2.dp
private val ThumbDefaultSize = 12.dp
private val ThumbDraggingSize = 16.dp
private val ThumbDisabledSize = 0.dp