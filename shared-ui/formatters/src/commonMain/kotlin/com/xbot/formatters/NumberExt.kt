package com.xbot.formatters

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.formatDecimal(digits: Int = 2): String {
    val factor = DECIMAL_BASE.pow(digits).toInt()
    val scaled = (this * factor).roundToInt()
    val whole = scaled / factor
    val fraction = (scaled % factor).toString().padStart(digits, '0')
    return if (digits == 0) whole.toString() else "$whole.$fraction"
}

fun Int.formatCompact(): String = when {
    this < THOUSAND -> toString()
    this < MILLION -> compact(THOUSAND) + "K"
    else -> compact(MILLION) + "M"
}

private fun Int.compact(unit: Int): String = (toDouble() / unit)
    .formatDecimal(digits = 1)
    .removeSuffix(".0")

private const val DECIMAL_BASE = 10.0
private const val THOUSAND = 1_000
private const val MILLION = 1_000_000
