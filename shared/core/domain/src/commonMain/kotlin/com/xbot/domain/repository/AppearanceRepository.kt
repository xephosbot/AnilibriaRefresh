package com.xbot.domain.repository

import com.xbot.domain.models.enums.ThemeOption
import kotlinx.coroutines.flow.Flow

interface AppearanceRepository {
    val themeOption: Flow<ThemeOption>
    val isDynamicTheme: Flow<Boolean>
    val isPureBlack: Flow<Boolean>
    val isExpressiveColor: Flow<Boolean>
    suspend fun setThemeOption(option: ThemeOption)
    suspend fun setDynamicTheme(enabled: Boolean)
    suspend fun setPureBlack(enabled: Boolean)
    suspend fun setExpressiveColor(enabled: Boolean)
}
