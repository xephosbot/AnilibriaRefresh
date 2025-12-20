package com.xbot.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.repository.AuthRepository
import com.xbot.resources.Res
import com.xbot.resources.login_success_message
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    val usernameState = TextFieldState()
    val passwordState = TextFieldState()

    private val _isLoading = MutableStateFlow(false)
    val state: StateFlow<LoginScreenState> = combine(
        _isLoading,
        authRepository.authState
    ) { isLoading, isAuthenticated ->
        LoginScreenState(
            isLoading = isLoading,
            isSuccess = isAuthenticated,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginScreenState()
    )

    private val _effects = Channel<LoginScreenEffect>()
    val effects = _effects.receiveAsFlow()

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.Login -> login()
            is LoginScreenAction.Logout -> viewModelScope.launch {
                authRepository.logout()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            val login = usernameState.text.toString()
            val password = passwordState.text.toString()

            when (val result = authRepository.login(login, password)) {
                is Either.Left -> {
                    _isLoading.value = false
                    snackbarManager.showMessage(
                        title = StringResource.String(result.value.toString())
                    )
                }
                is Either.Right -> {
                    _isLoading.value = false
                    snackbarManager.showMessage(
                        title = StringResource.Text(Res.string.login_success_message)
                    )
                    _effects.send(LoginScreenEffect.NavigateBack)
                }
            }
        }
    }
}

@Stable
data class LoginScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

@Stable
sealed interface LoginScreenAction {
    data object Login : LoginScreenAction
    data object Logout : LoginScreenAction
}

@Stable
sealed interface LoginScreenEffect {
    data object NavigateBack : LoginScreenEffect
}
