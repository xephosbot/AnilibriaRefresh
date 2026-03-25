package com.xbot.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.models.AuthState
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.LoginUseCase
import com.xbot.domain.usecase.LogoutUseCase
import com.xbot.localization.UiText
import com.xbot.resources.Res
import com.xbot.resources.login_success_message
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
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
import org.orbitmvi.orbit.syntax.simple.postSideEffect


@OptIn(OrbitExperimental::class)
@KoinViewModel
class LoginViewModel(
    private val getAuthState: GetAuthStateUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel(), ContainerHost<LoginScreenState, LoginScreenEffect> {

    val usernameState = TextFieldState()
    val passwordState = TextFieldState()

    override val container: Container<LoginScreenState, LoginScreenEffect> = container(
        initialState = LoginScreenState(),
    ) {
        startObservingAuth()
    }

    private fun startObservingAuth() = intent {
        getAuthState().collect { authState ->
            reduce {
                state.copy(
                    isSuccess = authState is AuthState.Authenticated,
                )
            }
        }
    }

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.Login -> login()
            is LoginScreenAction.Logout -> intent {
                logoutUseCase()
            }
        }
    }

    private fun login() = intent {
        reduce { state.copy(isLoading = true) }
        val loginText = usernameState.text.toString()
        val passwordText = passwordState.text.toString()

        when (val result = loginUseCase(loginText, passwordText)) {
            is Either.Left -> {
                reduce { state.copy(isLoading = false) }
                snackbarManager.showMessage(
                    title = UiText.String(result.value.toString())
                )
            }
            is Either.Right -> {
                reduce { state.copy(isLoading = false) }
                snackbarManager.showMessage(
                    title = UiText.Text(Res.string.login_success_message)
                )
                postSideEffect(LoginScreenEffect.NavigateBack)
            }
        }
    }
}

@Stable
internal data class LoginScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

@Stable
internal sealed interface LoginScreenAction {
    data object Login : LoginScreenAction
    data object Logout : LoginScreenAction
}

@Stable
internal sealed interface LoginScreenEffect {
    data object NavigateBack : LoginScreenEffect
}
