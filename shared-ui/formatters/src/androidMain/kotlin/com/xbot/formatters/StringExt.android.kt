package com.xbot.formatters

actual fun String.format(vararg args: Any?): String = java.lang.String.format(this, *args)
