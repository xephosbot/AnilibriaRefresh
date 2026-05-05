package com.xbot.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun clipEntryOf(string: String): ClipEntry = ClipEntry.withPlainText(string)