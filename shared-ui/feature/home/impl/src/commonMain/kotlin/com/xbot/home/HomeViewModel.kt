package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import arrow.core.Either
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.User
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getCatalogReleasesPager()
        .cachedIn(viewModelScope)

    private val refreshTrigger = MutableStateFlow(0)
    private val bestType = MutableStateFlow(BestType.Now)

    private val feedData = refreshTrigger
        .flatMapLatest { attempt ->
            flow {
                when (val result = getReleasesFeed()) {
                    is Either.Left -> {
                        showErrorMessage(result.value.toString()) { refresh() }
                        if (attempt == 0) {
                            emit(null)
                        }
                    }
                    is Either.Right -> emit(result.value)
                }
            }
        }

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

    val state: StateFlow<HomeScreenState> =
        combine(getAuthState(), feedData, scheduleData, bestType) { authState, feed, schedule, currentBestType ->
            val user = when (authState) {
                is AuthState.Authenticated -> authState.user
                is AuthState.Unauthenticated -> null
            }

            HomeScreenState(
                isFeedLoading = feed == null,
                isScheduleLoading = schedule == null,
                currentUser = user,
                releasesFeed = feed,
                scheduleWeek = schedule.orEmpty(),
                currentBestType = currentBestType,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = HomeScreenState()
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun updateBestType(bestType: BestType) {
        this.bestType.update { bestType }
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
data class HomeScreenState(
    val isFeedLoading: Boolean = true,
    val isScheduleLoading: Boolean = true,
    val currentUser: User? = null,
    val releasesFeed: ReleasesFeed? = null,
    val scheduleWeek: Map<LocalDate, List<Schedule>>? = null,
    val currentBestType: BestType = BestType.Now,
)

@Stable
sealed interface HomeScreenAction {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : HomeScreenAction

    @Stable
    data object Refresh : HomeScreenAction

    @Stable
    data class UpdateBestType(val bestType: BestType) : HomeScreenAction
}

@Stable
enum class BestType {
    Now, AllTime
}
