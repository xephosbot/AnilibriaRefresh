package com.xbot.preference.appearance

import androidx.lifecycle.ViewModel
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.usecase.GetDynamicThemeUseCase
import com.xbot.domain.usecase.GetExpressiveColorUseCase
import com.xbot.domain.usecase.GetPureBlackUseCase
import com.xbot.domain.usecase.GetThemeOptionUseCase
import com.xbot.domain.usecase.UpdateDynamicThemeUseCase
import com.xbot.domain.usecase.UpdateExpressiveColorUseCase
import com.xbot.domain.usecase.UpdatePureBlackUseCase
import com.xbot.domain.usecase.UpdateThemeOptionUseCase
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@KoinViewModel
class AppearanceViewModel(
    private val getThemeOptionUseCase: GetThemeOptionUseCase,
    private val getDynamicThemeUseCase: GetDynamicThemeUseCase,
    private val getPureBlackUseCase: GetPureBlackUseCase,
    private val getExpressiveColorUseCase: GetExpressiveColorUseCase,
    private val updateThemeOptionUseCase: UpdateThemeOptionUseCase,
    private val updateDynamicThemeUseCase: UpdateDynamicThemeUseCase,
    private val updatePureBlackUseCase: UpdatePureBlackUseCase,
    private val updateExpressiveColorUseCase: UpdateExpressiveColorUseCase,
) : ViewModel(), ContainerHost<AppearanceScreenState, Nothing> {

    override val container: Container<AppearanceScreenState, Nothing> = container(
        initialState = AppearanceScreenState()
    ) {
        startObservingAppearance()
    }

    private fun startObservingAppearance() = intent {
        combine(
            getThemeOptionUseCase(),
            getDynamicThemeUseCase(),
            getPureBlackUseCase(),
            getExpressiveColorUseCase()
        ) { themeOption, isDynamicTheme, isPureBlack, isExpressiveColor ->
            AppearanceScreenState(
                themeOption = themeOption,
                isDynamicTheme = isDynamicTheme,
                isPureBlack = isPureBlack,
                isExpressiveColor = isExpressiveColor
            )
        }.collect { newState ->
            reduce { newState }
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
