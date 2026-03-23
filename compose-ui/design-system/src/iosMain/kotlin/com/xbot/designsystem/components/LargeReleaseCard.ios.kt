package com.xbot.designsystem.components

import androidx.compose.foundation.OverscrollEffect
import com.xbot.designsystem.utils.CupertinoOverscrollEffect

internal actual fun getOverscrollOffset(overscrollEffect: OverscrollEffect): Float {
    return (overscrollEffect as? CupertinoOverscrollEffect)?.visibleOverscrollOffset?.y?.toFloat() ?: 0f
}