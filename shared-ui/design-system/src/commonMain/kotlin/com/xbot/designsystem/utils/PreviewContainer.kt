package com.xbot.designsystem.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.theme.AnilibriaTheme

@Composable
internal fun PreviewContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    val shimmer = rememberShimmer(ShimmerBounds.Window)

    AnilibriaTheme {
        ProvideShimmer(shimmer) {
            Surface {
                Column { content() }
            }
        }
    }
}