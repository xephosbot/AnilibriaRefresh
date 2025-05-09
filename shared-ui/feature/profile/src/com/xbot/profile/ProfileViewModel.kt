package com.xbot.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Profile
import com.xbot.domain.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Loading)
    val state: StateFlow<ProfileScreenState> = _state
        .onStart { fetchUserProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    fun onAction(action: ProfileScreenAction) {
        when(action) {
            is ProfileScreenAction.Login -> login(action.login, action.password)
            is ProfileScreenAction.Logout -> logout()
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile().fold(
                onSuccess = { profile ->
                    _state.update { ProfileScreenState.LoggedIn(profile) }
                },
                onFailure = {
                    logout()
                    showErrorMessage(it.message.orEmpty())
                }
            )
        }
    }

    private fun login(login: String, password: String) {
        viewModelScope.launch {
            repository.login(login, password).fold(
                onSuccess = {
                    fetchUserProfile()
                },
                onFailure = {
                    showErrorMessage(it.message.orEmpty())
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout().fold(
                onSuccess = {

                },
                onFailure = {
                    showErrorMessage(it.message.orEmpty())
                }
            )
            _state.update { ProfileScreenState.LoggedOut }
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
        val userProfile: Profile
    ) : ProfileScreenState
}

sealed interface ProfileScreenAction {
    data class Login(
        val login: String,
        val password: String
    ) : ProfileScreenAction

    data object Logout : ProfileScreenAction
}