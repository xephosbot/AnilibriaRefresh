package com.xbot.formatters

import androidx.compose.ui.text.intl.Locale
import com.ibm.icu.text.MeasureFormat
import com.ibm.icu.util.Measure
import com.ibm.icu.util.MeasureUnit
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
