package com.xbot.shared.ui.designsystem.modifier

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

fun Modifier.parallaxOffset(state: PagerState, page: Int) = this
    .graphicsLayer {
        val pageOffset = state.offsetForPage(page)
        val translation = size.width * (pageOffset / PARALLAX_MULTIPLIER)

        translationX = translation
        alpha = (2f - pageOffset.absoluteValue * PARALLAX_MULTIPLIER) / 2f
    }

internal fun PagerState.offsetForPage(page: Int) =
    (currentPage - page) + currentPageOffsetFraction

private const val PARALLAX_MULTIPLIER = 4f