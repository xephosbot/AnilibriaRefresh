package com.xbot.common

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

fun ClipboardManager.copyText(text: String?) {
    text?.let {
        setText(AnnotatedString(it))
    }
}
