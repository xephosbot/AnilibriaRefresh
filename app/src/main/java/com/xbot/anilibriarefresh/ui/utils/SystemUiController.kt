package com.xbot.anilibriarefresh.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.View
import androidx.annotation.IntDef
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun rememberSystemUiController(): SystemUiController {
    val view = LocalView.current
    val activity = view.context.findActivity()
    return remember(view, activity) { AndroidSystemUiController(view, activity) }
}

@Stable
interface SystemUiController {
    @SystemBarsBehavior
    var systemBarsBehavior: Int

    var isStatusBarVisible: Boolean

    var isNavigationBarVisible: Boolean

    var isSystemBarsVisible: Boolean
        get() = isStatusBarVisible && isNavigationBarVisible
        set(value) {
            isStatusBarVisible = value
            isNavigationBarVisible = value
        }

    @Orientation
    var requestedOrientation: Int
}

private class AndroidSystemUiController(
    private val view: View,
    private val activity: Activity?,
) : SystemUiController {
    private val window = (view.parent as? DialogWindowProvider)?.window
        ?: activity?.window
    private val windowInsetsController = window?.let {
        WindowCompat.getInsetsController(it, view)
    }

    override var systemBarsBehavior: Int
        get() = windowInsetsController?.systemBarsBehavior ?: 0
        set(value) {
            windowInsetsController?.systemBarsBehavior = value
        }

    override var isStatusBarVisible: Boolean
        get() {
            return ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.statusBars()) == true
        }
        set(value) {
            if (value) {
                windowInsetsController?.show(WindowInsetsCompat.Type.statusBars())
            } else {
                windowInsetsController?.hide(WindowInsetsCompat.Type.statusBars())
            }
        }

    override var isNavigationBarVisible: Boolean
        get() {
            return ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.navigationBars()) == true
        }
        set(value) {
            if (value) {
                windowInsetsController?.show(WindowInsetsCompat.Type.navigationBars())
            } else {
                windowInsetsController?.hide(WindowInsetsCompat.Type.navigationBars())
            }
        }

    override var requestedOrientation: Int
        get() = activity?.requestedOrientation ?: -1
        set(value) {
            activity?.requestedOrientation = value
        }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.PROPERTY)
@IntDef(WindowInsetsControllerCompat.BEHAVIOR_DEFAULT, WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
annotation class SystemBarsBehavior

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.PROPERTY)
@IntDef(
    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_USER,
    ActivityInfo.SCREEN_ORIENTATION_BEHIND,
    ActivityInfo.SCREEN_ORIENTATION_SENSOR,
    ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
    ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
    ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
    ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
    ActivityInfo.SCREEN_ORIENTATION_LOCKED,
)
annotation class Orientation
