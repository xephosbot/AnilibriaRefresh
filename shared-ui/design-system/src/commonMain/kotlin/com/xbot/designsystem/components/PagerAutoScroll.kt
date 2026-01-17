package com.xbot.designsystem.components

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun AutoScrollSideEffect(
    autoScrollDurationMillis: Long,
    pagerState: PagerState,
    doAutoScroll: Boolean,
    onAutoScrollChange: (isAutoScrollActive: Boolean) -> Unit = {},
) {
    if (autoScrollDurationMillis == Long.MAX_VALUE || autoScrollDurationMillis < 0) {
        return
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    if (doAutoScroll) {
        LaunchedEffect(pagerState, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    yield()
                    delay(autoScrollDurationMillis)
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }
            }
        }
    }
    onAutoScrollChange(doAutoScroll)
}

@Composable
fun shouldPerformAutoScroll(
    interactionSource: InteractionSource,
): Boolean {
    val pagerIsPressed by interactionSource.collectIsPressedAsState()
    val pagerIsDragged by interactionSource.collectIsDraggedAsState()

    return !(pagerIsPressed || pagerIsDragged)
}
