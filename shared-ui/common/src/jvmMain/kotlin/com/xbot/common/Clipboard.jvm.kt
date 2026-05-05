package com.xbot.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import java.awt.datatransfer.StringSelection

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun clipEntryOf(string: String): ClipEntry = ClipEntry(StringSelection(string))