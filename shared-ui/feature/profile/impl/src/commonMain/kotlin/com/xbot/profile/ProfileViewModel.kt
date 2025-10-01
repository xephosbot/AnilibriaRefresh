package com.xbot.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.User
import com.xbot.domain.repository.AuthRepository
import com.xbot.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val state: StateFlow<ProfileScreenState> = authRepository.observeAuthState()
        .map { isAuthenticated ->
            println("isAuthenticated: $isAuthenticated")
            if (isAuthenticated) {
                profileRepository.getProfile().fold(
                    ifRight = { profile ->
                        return@map ProfileScreenState.LoggedIn(profile)
                    },
                    ifLeft = {
                        logout()
                        showErrorMessage(it.toString())
                        return@map ProfileScreenState.LoggedOut
                    }
                )
            } else {
                ProfileScreenState.LoggedOut
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ProfileScreenState.Loading
        )

    fun onAction(action: ProfileScreenAction) {
        when(action) {
            is ProfileScreenAction.Login -> login(action.login, action.password)
            is ProfileScreenAction.Logout -> logout()
        }
    }

    private fun login(login: String, password: String) {
        viewModelScope.launch {
            authRepository.login(login, password).fold(
                ifRight = { /*Nothing to do*/
                    println("Login successful")
                },
                ifLeft = {
                    println("Failed to login")
                    showErrorMessage(it.toString().orEmpty())
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout().fold(
                ifRight = { /*Nothing to do*/ },
                ifLeft = {
                    showErrorMessage(it.toString().orEmpty())
                }
            )
        }
    }

    private fun showErrorMessage(error: String) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
        )
    }
}

sealed interface ProfileScreenState {
    data object Loading : ProfileScreenState
    data object LoggedOut : ProfileScreenState
    data class LoggedIn(
        val userProfile: User
    ) : ProfileScreenState
}

sealed interface ProfileScreenAction {
    data class Login(
        val login: String,
        val password: String
    ) : ProfileScreenAction

    data object Logout : ProfileScreenAction
}