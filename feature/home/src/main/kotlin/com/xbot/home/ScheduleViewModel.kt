package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

class ScheduleViewModel(
    private val releaseRepository: ReleaseRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<ScheduleScreenState> =
        MutableStateFlow(ScheduleScreenState.Loading)
    val state: StateFlow<ScheduleScreenState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun refresh() {
        viewModelScope.launch {
            _state.update { ScheduleScreenState.Loading }
            releaseRepository.getScheduleWeek().fold(
                onSuccess = { schedule ->
                    _state.update { ScheduleScreenState.Success(schedule) }
                },
                onFailure = {
                    showErrorMessage(it.localizedMessage.orEmpty(), ::refresh)
                }
            )
        }
    }

    private fun showErrorMessage(error: String, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
            action = MessageAction(
                title = StringResource.Text(R.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }
}

@Stable
sealed interface ScheduleScreenState {
    data object Loading : ScheduleScreenState
    data class Success(val schedule: Map<DayOfWeek, List<Release>>) : ScheduleScreenState
}
