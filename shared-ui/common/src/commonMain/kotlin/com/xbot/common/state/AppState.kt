package com.xbot.common.state

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.enums.ThemeOption

@Stable
interface AppState {
    val isOffline: Boolean
    val themeState: AppThemeState
    val authState: AuthState
}

data class AppThemeState(
    val themeOption: ThemeOption = ThemeOption.System,
    val isDynamicTheme: Boolean = false,
    val isPureBlack: Boolean = false,
    val isExpressiveColor: Boolean = false,
) {
    val isDarkTheme
        @Composable get() = when (themeOption) {
            ThemeOption.System -> isSystemInDarkTheme()
            ThemeOption.Dark -> true
            ThemeOption.Light -> false
        }
}

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No AppState provided")
}
