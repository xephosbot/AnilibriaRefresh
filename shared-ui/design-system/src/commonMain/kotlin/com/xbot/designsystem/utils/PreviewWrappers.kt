package com.xbot.designsystem.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.theme.AnilibriaTheme

class AnilibriaPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        val shimmer = rememberShimmer(ShimmerBounds.Window)
        AnilibriaTheme {
            ProvideShimmer(shimmer) {
                Surface {
                    Box(contentAlignment = Alignment.Center) {
                        content()
                    }
                }
            }
        }
    }
}
