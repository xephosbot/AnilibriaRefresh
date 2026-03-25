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

import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

@OptIn(OrbitExperimental::class)
@KoinViewModel
class AppearanceViewModel(
    private val getAppearanceSettingsUseCase: GetAppearanceSettingsUseCase,
    private val updateThemeOptionUseCase: UpdateThemeOptionUseCase,
    private val updateDynamicThemeUseCase: UpdateDynamicThemeUseCase,
    private val updatePureBlackUseCase: UpdatePureBlackUseCase,
    private val updateExpressiveColorUseCase: UpdateExpressiveColorUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AppearanceSettings, Nothing> {

    override val container: Container<AppearanceSettings, Nothing> = container(
        initialState = AppearanceSettings(),
    ) {
        startObservingAppearance()
    }

    private fun startObservingAppearance() = intent {
        getAppearanceSettingsUseCase().collect { settings ->
            reduce { settings }
        }
    }

    fun onAction(action: AppearanceScreenAction) {
        when (action) {
            is AppearanceScreenAction.OnThemeOptionChange -> onThemeOptionChange(action.option)
            is AppearanceScreenAction.OnDynamicThemeChange -> onDynamicThemeChange(action.enabled)
            is AppearanceScreenAction.OnPureBlackChange -> onPureBlackChange(action.enabled)
            is AppearanceScreenAction.OnExpressiveColorChange -> onExpressiveColorChange(action.enabled)
        }
    }

    private fun onThemeOptionChange(option: ThemeOption) = intent {
        updateThemeOptionUseCase(option)
    }

    private fun onDynamicThemeChange(enabled: Boolean) = intent {
        updateDynamicThemeUseCase(enabled)
    }

    private fun onPureBlackChange(enabled: Boolean) = intent {
        updatePureBlackUseCase(enabled)
    }

    private fun onExpressiveColorChange(enabled: Boolean) = intent {
        updateExpressiveColorUseCase(enabled)
    }
}

internal sealed interface AppearanceScreenAction {
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceScreenAction
    data class OnDynamicThemeChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnPureBlackChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnExpressiveColorChange(val enabled: Boolean) : AppearanceScreenAction
}
