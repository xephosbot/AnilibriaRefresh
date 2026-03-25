package com.xbot.login.state

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.xbot.domain.models.AuthState
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.LoginUseCase
import com.xbot.domain.usecase.LogoutUseCase
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel(binds = [ViewModel::class])
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
            is LoginScreenAction.Logout -> intent {
                logoutUseCase()
            }
        }
    }

    private fun login(username: String, password: String) = intent {
        reduce { state.copy(isLoading = true) }

        when (val result = loginUseCase(username, password)) {
            is Either.Left -> {
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginScreenSideEffect.ShowErrorMessage(result.value))
            }
            is Either.Right -> {
                reduce { state.copy(isLoading = false) }
                postSideEffect(LoginScreenSideEffect.LoginSuccess)
                postSideEffect(LoginScreenSideEffect.NavigateBack)
            }
        }
    }
}

@Serializable
@Stable
data class LoginScreenState(
    @Transient val isLoading: Boolean = false,
    @Transient val isSuccess: Boolean = false,
)

@Stable
sealed interface LoginScreenAction {
    @Stable
    data class Login(val username: String, val password: String) : LoginScreenAction

    @Stable
    data object Logout : LoginScreenAction
}

@Stable
sealed interface LoginScreenSideEffect {
    @Stable
    data object NavigateBack : LoginScreenSideEffect

    @Stable
    data class ShowErrorMessage(val error: Throwable) : LoginScreenSideEffect

    @Stable
    data object LoginSuccess : LoginScreenSideEffect
}
