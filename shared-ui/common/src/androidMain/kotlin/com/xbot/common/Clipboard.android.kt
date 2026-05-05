package com.xbot.common

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

internal actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry(ClipData.newPlainText("text", string))
}