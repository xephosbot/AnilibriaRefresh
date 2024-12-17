package com.xbot.anilibriarefresh.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

@Composable
fun OverlayHeaderLayout(
    modifier: Modifier = Modifier,
    scrollBehavior: OverlayHeaderLayoutScrollBehavior = rememberOverlayHeaderLayoutScrollBehavior(),
    headlineContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    DimensionSubcomposeLayout(
        content = headlineContent,
    ) { headlineContentSize ->
        SideEffect {
            if (scrollBehavior.state.heightOffsetLimit != headlineContentSize.height) {
                scrollBehavior.state.heightOffsetLimit = headlineContentSize.height
                scrollBehavior.state.heightOffset = headlineContentSize.height
            }
        }

        val dragModifier = Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta -> scrollBehavior.state.heightOffset += delta },
            onDragStopped = { velocity ->
                settleScroll(
                    scrollBehavior.state,
                    velocity,
                    scrollBehavior.flingAnimationSpec,
                    scrollBehavior.snapAnimationSpec
                )
            }
        )

        val contentBox = @Composable {
            Box(
                modifier = Modifier
                    .offset { IntOffset(0, scrollBehavior.state.heightOffset.toInt()) },
            ) {
                content()
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .then(dragModifier),
        ) {
            headlineContent()
            contentBox()
        }
    }
}

@Composable
fun rememberOverlayHeaderLayoutScrollBehavior(
    state: OverlayHeaderLayoutState = rememberOverlayHeaderLayoutState(),
    snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay(),
): OverlayHeaderLayoutScrollBehavior {
    return remember {
        OverlayHeaderLayoutScrollBehavior(
            state = state,
            snapAnimationSpec = snapAnimationSpec,
            flingAnimationSpec = flingAnimationSpec,
        )
    }
}

@Composable
fun rememberOverlayHeaderLayoutState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
): OverlayHeaderLayoutState {
    return remember {
        OverlayHeaderLayoutState(initialHeightOffsetLimit, initialHeightOffset)
    }
}

@Stable
class OverlayHeaderLayoutState(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
) {
    internal var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)
    internal var heightOffset: Float
        get() = _heightOffset.floatValue
        set(newOffset) {
            _heightOffset.floatValue =
                newOffset.coerceIn(minimumValue = 0f, maximumValue = heightOffsetLimit)
        }

    val collapsedFraction: Float
        get() =
            if (heightOffsetLimit != 0f) {
                heightOffset / heightOffsetLimit
            } else {
                0f
            }

    private var _heightOffset = mutableFloatStateOf(initialHeightOffset)
}

@Stable
class OverlayHeaderLayoutScrollBehavior(
    val state: OverlayHeaderLayoutState,
    val snapAnimationSpec: AnimationSpec<Float>?,
    val flingAnimationSpec: DecayAnimationSpec<Float>?,
) {
    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (available.y > 0f) return Offset.Zero

            val prevHeightOffset = state.heightOffset
            state.heightOffset += available.y
            return if (prevHeightOffset != state.heightOffset) {
                available.copy(x = 0f)
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (available.y < 0f || consumed.y < 0f) {
                val oldHeightOffset = state.heightOffset
                state.heightOffset += consumed.y
                return Offset(0f, state.heightOffset - oldHeightOffset)
            }
            if (available.y > 0f) {
                val oldHeightOffset = state.heightOffset
                state.heightOffset += consumed.y
                state.heightOffset += available.y
                return Offset(0f, state.heightOffset - oldHeightOffset)
            }
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            val superConsumed = super.onPostFling(consumed, available)
            return superConsumed +
                    settleScroll(state, available.y, flingAnimationSpec, snapAnimationSpec)
        }
    }
}

private suspend fun settleScroll(
    state: OverlayHeaderLayoutState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?,
): Velocity {
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 && state.heightOffset > state.heightOffsetLimit) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec,
            ) {
                state.heightOffset = value
            }
        }
    }

    return Velocity(0f, remainingVelocity)
}
