package com.xbot.localization

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun String.format(vararg args: Any?): String {
    if (args.isEmpty()) return this
    
    var result = this
    args.forEach { arg ->
        val pattern = "%[\\d|.]*[sdf]|[%][@]".toRegex()
        val match = pattern.find(result)
        if (match != null) {
            val formatted = when (arg) {
                is Double -> NSString.stringWithFormat(match.value, arg)
                is Float -> NSString.stringWithFormat(match.value, arg)
                is Int -> NSString.stringWithFormat(match.value, arg)
                is Long -> NSString.stringWithFormat(match.value, arg)
                else -> NSString.stringWithFormat("%@", arg)
            }
            result = result.replaceFirst(match.value, formatted)
        }
    }
    return result
}
