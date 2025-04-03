package com.xbot.player.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.coroutineScope
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
    track: @Composable (current: Float, buffered: Float) -> Unit = { current, buffered ->
        Track(played = current, buffered = buffered)
    },
) {
    val state = remember {
        TimeBarState(positionMs, durationMs, onPositionChange, onPositionChangeStart, onPositionChangeStop)
    }
    state.onPositionChange = onPositionChange
    state.onPositionChangeStart = onPositionChangeStart
    state.onPositionChangeStop = onPositionChangeStop
    state.position = positionMs.toFloat() / durationMs
    state.isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val drag = Modifier.draggable(
        orientation = Orientation.Horizontal,
        reverseDirection = state.isRtl,
        enabled = enabled,
        interactionSource = interactionSource,
        onDragStarted = { startedPosition ->
            state.gestureStartAction(startedPosition)
        },
        onDragStopped = {
            state.gestureEndAction()
        },
        startDragImmediately = state.isDragging,
        state = state,
    )

    val thumbBox = @Composable {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .onSizeChanged {
                    state.thumbWidth = it.width.toFloat()
                },
        ) {
            thumb(enabled, state.isDragging)
        }
    }
    val trackBox = @Composable {
        Box(
            modifier = Modifier.wrapContentWidth(),
        ) {
            track(
                if (durationMs > 0) state.position else 0f,
                if (durationMs > 0) bufferedPositionMs.toFloat() / durationMs else 0f,
            )
        }
    }

    TimeBarLayout(
        modifier = modifier.then(drag),
        state = state,
        thumb = thumbBox,
        track = trackBox,
    )
}

@Composable
private fun TimeBarLayout(
    modifier: Modifier = Modifier,
    state: TimeBarState,
    thumb: @Composable () -> Unit,
    track: @Composable () -> Unit,
) {
    Layout(
        modifier = Modifier
            .requiredSizeIn(
                minWidth = ThumbDefaultSize,
                minHeight = ThumbDefaultSize,
            )
            .then(modifier),
        contents = listOf(thumb, track),
    ) { (thumbMeasurable, trackMeasurable), constraints ->
        val thumbPlaceable = thumbMeasurable.first()
            .measure(constraints)
        val trackPlaceable = trackMeasurable.first()
            .measure(constraints.copy(minHeight = 0))

        val sliderWidth = constraints.maxWidth
        val sliderHeight = ThumbDraggingSize.roundToPx()

        state.updateDimensions(trackPlaceable.height.toFloat(), sliderWidth)

        val trackOffsetX = 0
        val thumbOffsetX = (state.position * sliderWidth).roundToInt()
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
            .rotate(if (LocalLayoutDirection.current == LayoutDirection.Rtl) 180f else 0f),
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
                size = size.copy(width = playedWidth),
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
                size = size.copy(width = bufferedWidth),
            )
            left = bufferedRight
        }
        // draw unplayed
        if (left < size.width) {
            drawRect(
                backgroundColor,
                topLeft = Offset(left, 0f),
                size = size.copy(width = size.width - left),
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
    shape: Shape = CircleShape,
) {
    val size = when {
        !enabled -> ThumbDisabledSize
        dragging -> ThumbDraggingSize
        else -> ThumbDefaultSize
    }
    Spacer(
        modifier = modifier
            .size(size)
            .background(color, shape),
    )
}

@Stable
class TimeBarState(
    position: Long,
    private val duration: Long,
    var onPositionChange: ((positionMs: Long) -> Unit)?,
    var onPositionChangeStart: ((positionMs: Long) -> Unit)?,
    var onPositionChangeStop: ((positionMs: Long) -> Unit)?,
) : DraggableState {
    private val valueRange: ClosedFloatingPointRange<Float> = 0f..1f

    private var positionState by mutableFloatStateOf(position.toFloat() / duration)
    private var dragPosition by mutableFloatStateOf(position.toFloat() / duration)
    internal var isDragging by mutableStateOf(false)
        private set

    var position: Float
        set(newVal) {
            val coercedValue = newVal.coerceIn(valueRange.start, valueRange.endInclusive)
            positionState = coercedValue
        }
        get() = if (isDragging) dragPosition else positionState

    private var totalWidth by mutableIntStateOf(0)
    internal var isRtl = false
    internal var trackHeight by mutableFloatStateOf(0f)
    internal var thumbWidth by mutableFloatStateOf(0f)

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit,
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        val targetProgress = ((position * totalWidth + delta) / totalWidth)
            .coerceIn(0f, 1f)
        dragPosition = targetProgress
        if (onPositionChange != null) {
            onPositionChange?.invoke((dragPosition * duration).roundToLong())
        }
    }

    internal fun updateDimensions(newTrackHeight: Float, newTotalWidth: Int) {
        trackHeight = newTrackHeight
        totalWidth = newTotalWidth
    }

    internal val gestureStartAction: (Offset) -> Unit = { startPosition ->
        val startX = startPosition.x - thumbWidth
        dragPosition = startX / totalWidth
        onPositionChangeStart?.invoke((dragPosition * duration).roundToLong())
    }

    internal val gestureEndAction = {
        onPositionChangeStop?.invoke((dragPosition * duration).roundToLong())
    }

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }
    private val scrollMutex = MutatorMutex()
}

private val TrackHeight = 2.dp
private val ThumbDefaultSize = 12.dp
private val ThumbDraggingSize = 16.dp
private val ThumbDisabledSize = 0.dp
