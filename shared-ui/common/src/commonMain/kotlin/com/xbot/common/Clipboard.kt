package com.xbot.common

import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

suspend fun Clipboard.copyText(text: String?) {
    text?.let {
        setClipEntry(clipEntryOf(text))
    }
}

internal expect fun clipEntryOf(string: String): ClipEntry
