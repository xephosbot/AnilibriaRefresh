package com.xbot.preference.appearance

import androidx.lifecycle.SavedStateHandle
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

internal class AppearanceViewModel(
    getAppearanceSettingsUseCase: GetAppearanceSettingsUseCase,
    private val updateThemeOptionUseCase: UpdateThemeOptionUseCase,
    private val updateDynamicThemeUseCase: UpdateDynamicThemeUseCase,
    private val updatePureBlackUseCase: UpdatePureBlackUseCase,
    private val updateExpressiveColorUseCase: UpdateExpressiveColorUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state: StateFlow<AppearanceSettings> = getAppearanceSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = AppearanceSettings()
        )

    fun onAction(action: AppearanceScreenAction) {
        when (action) {
            is AppearanceScreenAction.OnThemeOptionChange -> onThemeOptionChange(action.option)
            is AppearanceScreenAction.OnDynamicThemeChange -> onDynamicThemeChange(action.enabled)
            is AppearanceScreenAction.OnPureBlackChange -> onPureBlackChange(action.enabled)
            is AppearanceScreenAction.OnExpressiveColorChange -> onExpressiveColorChange(action.enabled)
        }
    }

    private fun onThemeOptionChange(option: ThemeOption) {
        viewModelScope.launch {
            updateThemeOptionUseCase(option)
        }
    }

    private fun onDynamicThemeChange(enabled: Boolean) {
        viewModelScope.launch {
            updateDynamicThemeUseCase(enabled)
        }
    }

    private fun onPureBlackChange(enabled: Boolean) {
        viewModelScope.launch {
            updatePureBlackUseCase(enabled)
        }
    }

    private fun onExpressiveColorChange(enabled: Boolean) {
        viewModelScope.launch {
            updateExpressiveColorUseCase(enabled)
        }
    }
}

internal sealed interface AppearanceScreenAction {
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceScreenAction
    data class OnDynamicThemeChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnPureBlackChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnExpressiveColorChange(val enabled: Boolean) : AppearanceScreenAction
}
