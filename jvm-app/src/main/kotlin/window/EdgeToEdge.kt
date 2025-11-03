package window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.platform.LocalPlatformWindowInsets
import androidx.compose.ui.platform.PlatformInsets
import androidx.compose.ui.platform.PlatformWindowInsets
import androidx.compose.ui.window.FrameWindowScope
import utils.Platform

fun FrameWindowScope.enableEdgeToEdge() {
    when (Platform.getCurrent()) {
        Platform.MacOS -> enableEdgeToEdgeMacOS(window)
        Platform.Windows -> Unit
        Platform.Linux -> Unit
    }
}

private fun enableEdgeToEdgeMacOS(window: ComposeWindow) {
    window.rootPane.putClientProperty("apple.awt.application.appearance", "system")
    window.rootPane.putClientProperty("apple.awt.fullscreenable", true)
    window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
    window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
    window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
}

@OptIn(InternalComposeUiApi::class)
@Composable
fun ProvidePlatformWindowInsets(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalPlatformWindowInsets provides MacOSWindowInsets) {
        content()
    }
}

@OptIn(InternalComposeUiApi::class)
private object MacOSWindowInsets : PlatformWindowInsets {
    override val systemBars: PlatformInsets
        get() = PlatformInsets(top = 28)
}

