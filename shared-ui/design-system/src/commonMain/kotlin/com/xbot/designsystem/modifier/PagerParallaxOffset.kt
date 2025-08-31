package com.xbot.designsystem.modifier

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

fun Modifier.horizontalParallax(state: PagerState, page: Int) = this
    .graphicsLayer {
        val pageOffset = state.offsetForPage(page)
        val translation = size.width * (pageOffset / PARALLAX_MULTIPLIER)

        translationX = translation
    }

fun Modifier.verticalParallax(state: LazyGridState) = this
    .graphicsLayer {
        val firstItemTranslationY = when {
            state.layoutInfo.visibleItemsInfo.isNotEmpty() && state.firstVisibleItemIndex == 0 ->
                state.firstVisibleItemScrollOffset * 0.7f

            else -> 0f
        }

        translationY = firstItemTranslationY
    }

fun Modifier.fadeWithParallax(state: PagerState, page: Int) = this
    .graphicsLayer {
        val pageOffset = state.offsetForPage(page)

        alpha = (2f - pageOffset.absoluteValue * PARALLAX_MULTIPLIER * 2f) / 2f
    }

internal fun PagerState.offsetForPage(page: Int) =
    (currentPage - page) + currentPageOffsetFraction

private const val PARALLAX_MULTIPLIER = 1.5f