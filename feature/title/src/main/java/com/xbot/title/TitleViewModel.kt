package com.xbot.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.repository.ReleaseRepository
import com.xbot.title.navigation.TitleRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitleViewModel(
    private val repository: ReleaseRepository,
    private val snackbarManager: SnackbarManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val titleId = savedStateHandle.toRoute<TitleRoute>().id

    private val _state: MutableStateFlow<TitleScreenState> =
        MutableStateFlow(TitleScreenState.Loading)
    val state: StateFlow<TitleScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    fun onAction(action: TitleScreenAction) {
        // TODO: Actions handling
    }

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            repository.getRelease(titleId).fold(
                onSuccess = { title ->
                    _state.update { TitleScreenState.Success(title = title) }
                },
                onFailure = {
                    showErrorMessage(it.localizedMessage.orEmpty())
                }
            )
        }
    }

    private fun showErrorMessage(error: String) {
        snackbarManager.showMessage(
            title = StringResource.String(error)
        )
    }
}

@Stable
sealed interface TitleScreenState {
    data object Loading : TitleScreenState
    data class Success(
        val title: ReleaseDetail,
    ) : TitleScreenState
}

sealed interface TitleScreenAction {
    // TODO: Actions for title screen
}
