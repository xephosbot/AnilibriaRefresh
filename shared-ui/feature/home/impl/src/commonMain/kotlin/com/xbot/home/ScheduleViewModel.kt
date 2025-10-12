package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Schedule
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ScheduleViewModel(
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<ScheduleScreenState> =
        MutableStateFlow(ScheduleScreenState.Loading)
    val state: StateFlow<ScheduleScreenState> = _state
        .onStart { fetch() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    fun onAction(action: ScheduleScreenAction) {
        when (action) {
            else -> {}
        }
    }

    private fun fetch() {
        viewModelScope.launch {
            _state.update { ScheduleScreenState.Loading }
            when (val result = getSortedScheduleWeekUseCase()) {
                is Either.Left -> showErrorMessage(result.value.toString(), ::fetch)
                is Either.Right -> _state.update { ScheduleScreenState.Success(result.value) }
            }
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
sealed interface ScheduleScreenState {
    data object Loading : ScheduleScreenState
    data class Success(val schedule: Map<LocalDate, List<Schedule>>) : ScheduleScreenState
}

@Stable
sealed interface ScheduleScreenAction {

}
