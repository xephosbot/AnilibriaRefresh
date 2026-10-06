package com.xbot.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import com.xbot.localization.LocalAppLanguage
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun Double.formatDecimal(digits: Int = 2): String =
    formatDecimal(Locale(LocalAppLanguage.current), digits)

fun Double.formatDecimal(locale: Locale, digits: Int = 2): String {
    val factor = DECIMAL_BASE.pow(digits).toInt()
    val scaled = (this * factor).roundToInt()
    val whole = scaled / factor
    val fraction = (scaled % factor).toString().padStart(digits, '0')
    return if (digits == 0) whole.toString() else "$whole${locale.decimalSeparator()}$fraction"
}

@Composable
fun Int.formatCompact(): String = formatCompact(Locale(LocalAppLanguage.current))

fun Int.formatCompact(locale: Locale): String = when {
    this < THOUSAND -> toString()
    this < MILLION -> compact(THOUSAND, locale) + "K"
    else -> compact(MILLION, locale) + "M"
}

private fun Int.compact(unit: Int, locale: Locale): String = (toDouble() / unit)
    .formatDecimal(locale, digits = 1)
    .removeSuffix("${locale.decimalSeparator()}0")

private fun Locale.decimalSeparator(): Char = if (language == "en") '.' else ','

private const val DECIMAL_BASE = 10.0
private const val THOUSAND = 1_000
private const val MILLION = 1_000_000
