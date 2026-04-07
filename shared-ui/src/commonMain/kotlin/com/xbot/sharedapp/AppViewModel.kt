package com.xbot.sharedapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.usecase.GetDynamicThemeUseCase
import com.xbot.domain.usecase.GetExpressiveColorUseCase
import com.xbot.domain.usecase.GetPureBlackUseCase
import com.xbot.domain.usecase.GetThemeOptionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class AppViewModel(
    getThemeOptionUseCase: GetThemeOptionUseCase,
    getDynamicThemeUseCase: GetDynamicThemeUseCase,
    getPureBlackUseCase: GetPureBlackUseCase,
    getExpressiveColorUseCase: GetExpressiveColorUseCase,
) : ViewModel() {
    val state: StateFlow<AppThemeState> = combine(
        getThemeOptionUseCase(),
        getDynamicThemeUseCase(),
        getPureBlackUseCase(),
        getExpressiveColorUseCase()
    ) { themeOption, isDynamicTheme, isPureBlack, isExpressiveColor ->
        AppThemeState(
            themeOption = themeOption,
            isDynamicTheme = isDynamicTheme,
            isPureBlack = isPureBlack,
            isExpressiveColor = isExpressiveColor
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppThemeState()
    )
}

data class AppThemeState(
    val themeOption: ThemeOption = ThemeOption.System,
    val isDynamicTheme: Boolean = false,
    val isPureBlack: Boolean = false,
    val isExpressiveColor: Boolean = false
)
