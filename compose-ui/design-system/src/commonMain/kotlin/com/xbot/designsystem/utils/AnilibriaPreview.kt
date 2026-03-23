package com.xbot.designsystem.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.theme.AnilibriaTheme

@Composable
fun AnilibriaPreview(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    AnilibriaTheme(
        darkTheme = useDarkTheme,
        dynamicColor = useDynamicColor,
    ) {
        ProvideShimmer(shimmer) {
            Surface(modifier = modifier) {
                Box(contentAlignment = Alignment.Center) {
                    content()
                }
            }
        }
    }
}
