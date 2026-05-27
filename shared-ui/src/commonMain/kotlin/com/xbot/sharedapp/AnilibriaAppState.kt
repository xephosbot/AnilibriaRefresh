package com.xbot.sharedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.xbot.common.state.AppState
import com.xbot.common.state.AppThemeState
import com.xbot.domain.models.AuthState
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
internal class AnilibriaAppState(
    private val connectivityState: ConnectivityState,
    private val getThemeOptionUseCase: GetThemeOptionUseCase,
    private val getDynamicThemeUseCase: GetDynamicThemeUseCase,
    private val getPureBlackUseCase: GetPureBlackUseCase,
    private val getExpressiveColorUseCase: GetExpressiveColorUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    coroutineScope: CoroutineScope,
) : AppState {
    override val isOffline: Boolean get() = connectivityState.isDisconnected

    override var themeState: AppThemeState by mutableStateOf(AppThemeState())
        private set

    override var authState: AuthState by mutableStateOf(AuthState.Unauthenticated(null))
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

@Composable
fun rememberAnilibriaAppState(
    connectivityState: ConnectivityState = rememberConnectivityState(koinInject()),
    getThemeOptionUseCase: GetThemeOptionUseCase = koinInject(),
    getDynamicThemeUseCase: GetDynamicThemeUseCase = koinInject(),
    getPureBlackUseCase: GetPureBlackUseCase = koinInject(),
    getExpressiveColorUseCase: GetExpressiveColorUseCase = koinInject(),
    getAuthStateUseCase: GetAuthStateUseCase = koinInject(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
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
