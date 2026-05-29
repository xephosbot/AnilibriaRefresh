package com.xbot.designsystem.utils

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

val LocalIsSinglePane = staticCompositionLocalOf { true }

val LocalNavSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("LocalNavSharedTransitionScope not provided")
}
