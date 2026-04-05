package com.xbot.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.domain.models.AuthState
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.LoginUseCase
import com.xbot.domain.usecase.LogoutUseCase
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

class LoginViewModel(
    private val getAuthState: GetAuthStateUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val state: StateFlow<LoginScreenState> = combine(
        _isLoading,
        getAuthState()
    ) { isLoading, authState ->
        LoginScreenState(
            isLoading = isLoading,
            isSuccess = authState is AuthState.Authenticated,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        initialValue = LoginScreenState()
    )

    private val _effects = Channel<LoginScreenEffect>()
    val effects = _effects.receiveAsFlow()

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.UsernameChanged -> _username.value = action.text
            is LoginScreenAction.PasswordChanged -> _password.value = action.text
            is LoginScreenAction.Login -> login()
            is LoginScreenAction.Logout -> viewModelScope.launch {
                logoutUseCase()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = loginUseCase(_username.value, _password.value)) {
                is Either.Left -> {
                    _isLoading.value = false
                    _effects.send(LoginScreenEffect.ShowError(result.value.toString()))
                }
                is Either.Right -> {
                    _isLoading.value = false
                    _effects.send(LoginScreenEffect.LoginSuccess)
                    _effects.send(LoginScreenEffect.NavigateBack)
                }
            }
        }
    }
}

data class LoginScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

sealed interface LoginScreenAction {
    data class UsernameChanged(val text: String) : LoginScreenAction
    data class PasswordChanged(val text: String) : LoginScreenAction
    data object Login : LoginScreenAction
    data object Logout : LoginScreenAction
}

sealed interface LoginScreenEffect {
    data object NavigateBack : LoginScreenEffect
    data object LoginSuccess : LoginScreenEffect
    data class ShowError(val message: String) : LoginScreenEffect
}
