package com.xbot.shared.ui.localization

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun String.format(vararg args: Any?): String {
    var returnString = ""
    val regex = "%[\\d|.]*[sdf]|[%]".toRegex()
    val singleFormats = regex.findAll(this).map {
        it.groupValues.first()
    }.asSequence().toList()
    val newStrings = this.split(regex)
    args.forEachIndexed { index, arg ->
        returnString += when (arg) {
            is Double -> {
                NSString.stringWithFormat(newStrings[index] + singleFormats[index], arg)
            }
            is Int -> {
                NSString.stringWithFormat(newStrings[index] + singleFormats[index], arg)
            }
            else -> {
                NSString.stringWithFormat(newStrings[index] + "%@", arg)
            }
        }
    }
    return returnString
}