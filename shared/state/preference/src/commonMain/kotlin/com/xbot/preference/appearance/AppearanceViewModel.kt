package com.xbot.preference.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.models.AppearanceSettings
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.usecase.GetAppearanceSettingsUseCase
import com.xbot.domain.usecase.UpdateDynamicThemeUseCase
import com.xbot.domain.usecase.UpdateExpressiveColorUseCase
import com.xbot.domain.usecase.UpdatePureBlackUseCase
import com.xbot.domain.usecase.UpdateThemeOptionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AppearanceViewModel(
    private val getAppearanceSettingsUseCase: GetAppearanceSettingsUseCase,
    private val updateThemeOptionUseCase: UpdateThemeOptionUseCase,
    private val updateDynamicThemeUseCase: UpdateDynamicThemeUseCase,
    private val updatePureBlackUseCase: UpdatePureBlackUseCase,
    private val updateExpressiveColorUseCase: UpdateExpressiveColorUseCase,
) : ViewModel() {

    val state: StateFlow<AppearanceSettings> = getAppearanceSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = AppearanceSettings()
        )

    fun onAction(action: AppearanceScreenAction) {
        when (action) {
            is AppearanceScreenAction.OnThemeOptionChange -> viewModelScope.launch { updateThemeOptionUseCase(action.option) }
            is AppearanceScreenAction.OnDynamicThemeChange -> viewModelScope.launch { updateDynamicThemeUseCase(action.enabled) }
            is AppearanceScreenAction.OnPureBlackChange -> viewModelScope.launch { updatePureBlackUseCase(action.enabled) }
            is AppearanceScreenAction.OnExpressiveColorChange -> viewModelScope.launch { updateExpressiveColorUseCase(action.enabled) }
        }
    }
}

sealed interface AppearanceScreenAction {
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceScreenAction
    data class OnDynamicThemeChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnPureBlackChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnExpressiveColorChange(val enabled: Boolean) : AppearanceScreenAction
}
