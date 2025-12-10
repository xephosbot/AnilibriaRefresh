package com.xbot.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitleViewModel(
    private val repository: ReleasesRepository,
    private val snackbarManager: SnackbarManager,
    private val aliasOrId: String,
) : ViewModel() {

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
            repository.getRelease(aliasOrId).fold(
                ifRight = { title ->
                    _state.update { TitleScreenState.Success(title = title) }
                },
                ifLeft = {
                    showErrorMessage(it.toString(), ::fetchTitleDetails)
                }
            )
        }
    }

    private fun showErrorMessage(error: String, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
            action = MessageAction(
                title = StringResource.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
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
