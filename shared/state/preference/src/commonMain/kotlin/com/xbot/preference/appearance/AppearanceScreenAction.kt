package com.xbot.preference.appearance

import com.xbot.domain.models.enums.ThemeOption

sealed interface AppearanceScreenAction {
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceScreenAction
    data class OnDynamicThemeChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnPureBlackChange(val enabled: Boolean) : AppearanceScreenAction
    data class OnExpressiveColorChange(val enabled: Boolean) : AppearanceScreenAction
}
