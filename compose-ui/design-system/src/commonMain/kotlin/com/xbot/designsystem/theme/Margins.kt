package com.xbot.designsystem.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Margins(
    val horizontal: Dp
)

@Stable
fun Margins.asPaddingValues(): PaddingValues = PaddingValues(horizontal = horizontal)

val LocalMargins = staticCompositionLocalOf { Margins(horizontal = 0.dp) }
