package com.xbot.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xbot.domain.models.AuthState
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.LoginUseCase
import com.xbot.domain.usecase.LogoutUseCase
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class LoginViewModel(
    private val getAuthState: GetAuthStateUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<LoginScreenState, LoginScreenSideEffect> {

    override val container: Container<LoginScreenState, LoginScreenSideEffect> = container(
        initialState = LoginScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = LoginScreenState.serializer(),
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
            is LoginScreenAction.Login -> login(action.username, action.password)
            is LoginScreenAction.Logout -> logout()
        }
    }

    private fun login(username: String, password: String) = intent {
        reduce { state.copy(isLoading = true) }

        loginUseCase(username, password).fold(
            ifLeft = {
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginScreenSideEffect.ShowErrorMessage(it))
            },
            ifRight = {
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginScreenSideEffect.LoginSuccess)
                postSideEffect(LoginScreenSideEffect.NavigateBack)
            }
        )
    }

    private fun logout() = intent {
        logoutUseCase()
    }
}
