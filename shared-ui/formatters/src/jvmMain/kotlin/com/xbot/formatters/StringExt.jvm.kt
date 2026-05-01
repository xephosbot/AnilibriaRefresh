package com.xbot.formatters

actual fun String.format(vararg args: Any?): String {
    return java.lang.String.format(this, *args)
}