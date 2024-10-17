package com.xbot.anilibriarefresh.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

val LocalShimmer = compositionLocalOf<Shimmer?> { null }

@Composable
fun ProvideShimmer(value: Shimmer, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalShimmer provides value, content = content)
}

@Composable
fun Modifier.shimmerSafe(shimmer: Shimmer?) = then(
    if (shimmer != null) Modifier.shimmer(shimmer)
    else Modifier
)