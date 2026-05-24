package com.xbot.sharedapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetDynamicThemeUseCase
import com.xbot.domain.usecase.GetExpressiveColorUseCase
import com.xbot.domain.usecase.GetPureBlackUseCase
import com.xbot.domain.usecase.GetThemeOptionUseCase
import dev.jordond.connectivity.compose.ConnectivityState
import dev.jordond.connectivity.compose.rememberConnectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Stable
class AnilibriaAppState(
    private val connectivityState: ConnectivityState,
    private val getThemeOptionUseCase: GetThemeOptionUseCase,
    private val getDynamicThemeUseCase: GetDynamicThemeUseCase,
    private val getPureBlackUseCase: GetPureBlackUseCase,
    private val getExpressiveColorUseCase: GetExpressiveColorUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    coroutineScope: CoroutineScope,
) {
    val isOffline: Boolean get() = connectivityState.isDisconnected

    var themeState: AppThemeState by mutableStateOf(AppThemeState())
        private set

    var authState: AuthState by mutableStateOf(AuthState.Unauthenticated(null))
        private set

    init {
        coroutineScope.launch {
            combine(
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
            }.collect {
                themeState = it
            }
        }

        coroutineScope.launch {
            getAuthStateUseCase().collect {
                authState = it
            }
        }
    }
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

@Composable
fun rememberAnilibriaAppState(
    connectivityState: ConnectivityState = rememberConnectivityState(koinInject()),
    getThemeOptionUseCase: GetThemeOptionUseCase = koinInject(),
    getDynamicThemeUseCase: GetDynamicThemeUseCase = koinInject(),
    getPureBlackUseCase: GetPureBlackUseCase = koinInject(),
    getExpressiveColorUseCase: GetExpressiveColorUseCase = koinInject(),
    getAuthStateUseCase: GetAuthStateUseCase = koinInject(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AnilibriaAppState {
    return remember(
        connectivityState,
        getThemeOptionUseCase,
        getDynamicThemeUseCase,
        getPureBlackUseCase,
        getExpressiveColorUseCase,
        getAuthStateUseCase,
        coroutineScope
    ) {
        AnilibriaAppState(
            connectivityState = connectivityState,
            getThemeOptionUseCase = getThemeOptionUseCase,
            getDynamicThemeUseCase = getDynamicThemeUseCase,
            getPureBlackUseCase = getPureBlackUseCase,
            getExpressiveColorUseCase = getExpressiveColorUseCase,
            getAuthStateUseCase = getAuthStateUseCase,
            coroutineScope = coroutineScope
        )
    }
}
