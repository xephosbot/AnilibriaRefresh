package com.xbot.formatters

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration

actual fun Duration.toLocalizedUnits(locale: Locale): String = toComponents {
        hours,
        minutes,
        _,
        _
    ->
    val measures = buildList {
        if (hours > 0) add(Measure(hours, MeasureUnit.HOUR))
        if (minutes > 0 || hours == 0L) add(Measure(minutes, MeasureUnit.MINUTE))
    }
    MeasureFormat.getInstance(locale.platformLocale, MeasureFormat.FormatWidth.SHORT)
        .formatMeasures(*measures.toTypedArray())
}
