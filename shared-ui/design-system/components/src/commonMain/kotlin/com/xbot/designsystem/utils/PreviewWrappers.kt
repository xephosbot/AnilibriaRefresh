package com.xbot.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.xbot.designsystem.modifier.ProvideShimmer
import com.xbot.designsystem.theme.AnilibriaTheme

@Preview(uiMode = UI_MODE_NIGHT_NO, name = "Light theme", showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark theme", showBackground = true)
@PreviewWrapper(ThemeAndShimmerWrapper::class)
annotation class AnilibriaPreview

internal class ThemeAndShimmerWrapper : PreviewWrapperProvider {
    private val themeWrapper = ThemeWrapper()
    private val shimmerWrapper = ShimmerWrapper()

    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        themeWrapper.Wrap { shimmerWrapper.Wrap { content() } }
    }
}

internal class ThemeWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        AnilibriaTheme {
            content()
        }
    }
}

internal class ShimmerWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        val shimmer = rememberShimmer(ShimmerBounds.Window)
        ProvideShimmer(shimmer) {
            content()
        }
    }
}

// Copy from Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_YES
private const val UI_MODE_NIGHT_NO = 0x10
private const val UI_MODE_NIGHT_YES = 0x20
