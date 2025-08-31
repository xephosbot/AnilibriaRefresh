package com.xbot.localization

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle

@RequiresApi(Build.VERSION_CODES.O)
actual fun DayOfWeek.getName(locale: Locale): String {
    return this.toJavaDayOfWeek().getDisplayName(TextStyle.FULL, locale.platformLocale)
}