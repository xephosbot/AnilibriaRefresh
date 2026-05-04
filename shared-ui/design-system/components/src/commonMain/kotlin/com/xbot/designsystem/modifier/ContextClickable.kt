package com.xbot.designsystem.modifier

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput

/**
 * A [Modifier] that handles contextual interactions across platforms:
 * - Long-press for touch interactions.
 * - Right-click (Secondary Button) for mouse interactions.
 *
 * @param enabled Whether the contextual interaction is enabled.
 * @param onContextClick Callback triggered when a contextual interaction occurs.
 */
fun Modifier.contextClickable(
    enabled: Boolean = true,
    onContextClick: () -> Unit
): Modifier = this
    .pointerInput(enabled, onContextClick) {
        if (!enabled) return@pointerInput
        detectTapGestures(
            onLongPress = { onContextClick() }
        )
    }
    .pointerInput(enabled, onContextClick) {
        if (!enabled) return@pointerInput
        awaitEachGesture {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull() ?: return@awaitEachGesture

            if (
                event.type == PointerEventType.Press &&
                event.buttons.isSecondaryPressed &&
                change.type == PointerType.Mouse
            ) {
                change.consume()
                waitForUpOrCancellation()
                onContextClick()
            }
        }
    }
