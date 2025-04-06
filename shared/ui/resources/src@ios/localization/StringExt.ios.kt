package com.xbot.resources.localization

import platform.Foundation.NSString

actual fun String.format(vararg args: Any): String {
    @Suppress("MagicNumber")
    return when (args.size) {
        0 -> NSString.stringWithFormat(this)
        1 -> NSString.stringWithFormat(this, args[0].cstr)
        2 -> NSString.stringWithFormat(this, args[0].cstr, args[1].cstr)
        3 -> NSString.stringWithFormat(this, args[0].cstr, args[1].cstr, args[2].cstr)
        4 -> NSString.stringWithFormat(this, args[0].cstr, args[1].cstr, args[2].cstr, args[3].cstr)
        else -> NSString.stringWithFormat(this, args[0].cstr, args[1].cstr, args[2].cstr, args[3].cstr, args[4].cstr)
    }
}