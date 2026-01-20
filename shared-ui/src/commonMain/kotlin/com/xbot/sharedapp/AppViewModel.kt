package com.xbot.sharedapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.models.AppearanceSettings
import com.xbot.domain.usecase.GetAppearanceSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class AppViewModel(
    getAppearanceSettingsUseCase: GetAppearanceSettingsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val appearanceSettings: StateFlow<AppearanceSettings> = getAppearanceSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppearanceSettings()
        )
}
