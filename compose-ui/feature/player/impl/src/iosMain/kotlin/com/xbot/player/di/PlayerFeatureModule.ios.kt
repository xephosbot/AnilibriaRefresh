package com.xbot.player.di

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun createFullscreenDialogProperties(): DialogProperties {
    return DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false,
        usePlatformInsets = false,
        useSoftwareKeyboardInset = false,
        scrimColor = Color.Transparent,
        animateTransition = false,
    )
}