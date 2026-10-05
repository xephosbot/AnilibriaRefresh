package com.xbot.player.di

import androidx.compose.ui.window.DialogProperties

internal actual fun createFullscreenDialogProperties(): DialogProperties = DialogProperties(
    dismissOnClickOutside = false,
    usePlatformDefaultWidth = false,
    decorFitsSystemWindows = false
)
