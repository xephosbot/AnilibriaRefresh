package com.xbot.resources.localization

import java.util.Locale

actual fun String.format(vararg args: Any): String {
    return String.format(Locale.getDefault(), this, args)
}