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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModel(
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val refreshTrigger = MutableStateFlow(0)
    private val scheduleData = refreshTrigger
        .flatMapLatest {
            flow {
                when (val result = getSortedScheduleWeekUseCase()) {
                    is Either.Left -> {
                        showErrorMessage(result.value.toString()) { refresh() }
                        emit(null)
                    }
                    is Either.Right -> emit(result.value)
                }
            }
        }

    val state: StateFlow<ScheduleScreenState> = scheduleData
        .map { schedule ->
            ScheduleScreenState(
                isLoading = schedule != null,
                schedule = schedule.orEmpty()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ScheduleScreenState()
        )

    fun onAction(action: ScheduleScreenAction) {
        when (action) {
            is ScheduleScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
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
data class ScheduleScreenState(
    val isLoading: Boolean = true,
    val schedule: Map<LocalDate, List<Schedule>> = emptyMap(),
)

@Stable
sealed interface ScheduleScreenAction {
    data object Refresh : ScheduleScreenAction
}
