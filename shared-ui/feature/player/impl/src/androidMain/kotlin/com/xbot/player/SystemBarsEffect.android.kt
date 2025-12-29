package com.xbot.player

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
actual fun SystemBarsEffect() {
    val context = LocalContext.current
    val view = LocalView.current
    val configuration = LocalConfiguration.current

    val window = remember(view, context) { findWindow(context, view) }

    DisposableEffect(window) {
        onDispose {
            if (window != null) {
                // Ensure the view is still attached or at least use it to get the controller
                // If the view is detached, this might throw or fail, but onDispose usually runs before full detachment
                val controller = WindowCompat.getInsetsController(window, view)
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    LaunchedEffect(configuration.orientation, window) {
        if (window != null) {
            val controller = WindowCompat.getInsetsController(window, view)
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}

private fun findWindow(context: Context, view: View): Window? {
    // Try to find the window via DialogWindowProvider (used by Compose Dialogs)
    val dialogWindowProvider = view.parent as? DialogWindowProvider
    if (dialogWindowProvider != null) {
        return dialogWindowProvider.window
    }
    
    // Fallback to Activity window
    return context.findActivity().window
}

