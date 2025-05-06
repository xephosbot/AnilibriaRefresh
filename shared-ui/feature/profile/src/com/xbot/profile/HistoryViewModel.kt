package com.xbot.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<HistoryScreenState> =
        MutableStateFlow(HistoryScreenState.Loading)
    val state: StateFlow<HistoryScreenState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun refresh() {
        viewModelScope.launch {
            _state.update { HistoryScreenState.Loading }
            getReleasesFeed().fold(
                onSuccess = { releasesFeed ->
                    _state.update { HistoryScreenState.Success(releasesFeed) }
                },
                onFailure = {
                    showErrorMessage(it.message.orEmpty(), ::refresh)
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
sealed interface HistoryScreenState {
    data object Loading : HistoryScreenState
    data class Success(val releasesFeed: ReleasesFeed) : HistoryScreenState
}