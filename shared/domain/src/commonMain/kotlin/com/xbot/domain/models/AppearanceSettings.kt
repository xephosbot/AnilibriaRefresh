package com.xbot.domain.models

import com.xbot.domain.models.enums.ThemeOption

data class AppearanceSettings(
    val themeOption: ThemeOption = ThemeOption.System,
    val isDynamicTheme: Boolean = false,
    val isPureBlack: Boolean = false,
    val isExpressiveColor: Boolean = false
)