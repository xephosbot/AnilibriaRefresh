package com.xbot.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.xbot.shared.ui.AnilibriaApp

fun MainViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    }
) {
    AnilibriaApp()
}