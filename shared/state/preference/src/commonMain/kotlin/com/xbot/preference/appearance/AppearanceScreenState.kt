package com.xbot.preference.appearance

import com.xbot.domain.models.enums.ThemeOption

data class AppearanceScreenState(
    val themeOption: ThemeOption = ThemeOption.System,
    val isDynamicTheme: Boolean = false,
    val isPureBlack: Boolean = false,
    val isExpressiveColor: Boolean = false
)
