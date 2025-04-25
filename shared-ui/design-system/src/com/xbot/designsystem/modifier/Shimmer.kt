package com.xbot.designsystem.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.unclippedBoundsInWindow

@Composable
fun Modifier.shimmerSafe(shimmer: Shimmer?) = then(
    if (shimmer != null) {
        Modifier.shimmer(shimmer)
    } else {
        Modifier
    },
)

fun Modifier.shimmerUpdater(shimmer: Shimmer) = onGloballyPositioned {
    val position = it.unclippedBoundsInWindow()
    shimmer.updateBounds(position)
}

@Composable
fun ProvideShimmer(value: Shimmer, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalShimmer provides value, content = content)
}

val LocalShimmer = compositionLocalOf<Shimmer?> { null }
