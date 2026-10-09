package com.xbot.designsystem.modifier

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import kotlin.math.floor

context(scope: TabIndicatorScope)
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    matchContentSize: Boolean = false
): Modifier = with(scope) {
    tabIndicatorLayout { measurable, constraints, tabPositions ->
        if (tabPositions.isEmpty()) return@tabIndicatorLayout layout(constraints.maxWidth, 0) {}

        val position = (pagerState.currentPage + pagerState.currentPageOffsetFraction)
            .coerceIn(0f, tabPositions.lastIndex.toFloat())
        val index = floor(position).toInt()
        val fraction = position - index
        val current = tabPositions[index]
        val next = tabPositions.getOrElse(index + 1) { current }

        val width = lerp(
            current.indicatorWidth(matchContentSize),
            next.indicatorWidth(matchContentSize),
            fraction
        ).roundToPx()
        val start = lerp(
            current.indicatorStart(matchContentSize),
            next.indicatorStart(matchContentSize),
            fraction
        ).roundToPx()

        val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
        layout(constraints.maxWidth, placeable.height) {
            placeable.placeRelative(start, 0)
        }
    }
}

private fun TabPosition.indicatorWidth(matchContentSize: Boolean): Dp =
    if (matchContentSize) contentWidth else width

private fun TabPosition.indicatorStart(matchContentSize: Boolean): Dp =
    if (matchContentSize) left + (width - contentWidth) / 2 else left
