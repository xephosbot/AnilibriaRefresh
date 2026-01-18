package com.xbot.fixtures.repository

import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.repository.AppearanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAppearanceRepository : AppearanceRepository {

    private val _themeOption = MutableStateFlow(ThemeOption.System)
    override val themeOption: Flow<ThemeOption> = _themeOption.asStateFlow()

    private val _isDynamicTheme = MutableStateFlow(false)
    override val isDynamicTheme: Flow<Boolean> = _isDynamicTheme.asStateFlow()

    private val _isPureBlack = MutableStateFlow(true)
    override val isPureBlack: Flow<Boolean> = _isPureBlack.asStateFlow()

    private val _isExpressiveColor = MutableStateFlow(false)
    override val isExpressiveColor: Flow<Boolean> = _isExpressiveColor.asStateFlow()

    override suspend fun setThemeOption(option: ThemeOption) {
        _themeOption.value = option
    }

    override suspend fun setDynamicTheme(enabled: Boolean) {
        _isDynamicTheme.value = enabled
    }

    override suspend fun setPureBlack(enabled: Boolean) {
        _isPureBlack.value = enabled
    }

    override suspend fun setExpressiveColor(enabled: Boolean) {
        _isExpressiveColor.value = enabled
    }
}