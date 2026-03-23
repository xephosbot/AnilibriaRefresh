package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.ScheduleWeek
import com.xbot.domain.models.User
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import com.xbot.localization.UiText
import com.xbot.localization.localizedMessage
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val releases: Flow<PagingData<Release>> = refreshTrigger.flatMapLatest {
        getCatalogReleasesPager()
    }.cachedIn(viewModelScope)

    private val bestType = savedStateHandle.getStateFlow(BEST_TYPE_KEY, BestType.Now)

    private val feedData = refreshTrigger.flatMapLatest {
        getReleasesFeed().catch { showErrorMessage(it) { refresh() } }
    }

    private val scheduleData = refreshTrigger.flatMapLatest {
        getSortedScheduleWeekUseCase().catch { showErrorMessage(it) { refresh() } }
    }

    val state: StateFlow<HomeScreenState> =
        combine(getAuthState(), feedData, scheduleData, bestType) { authState, feed, schedule, currentBestType ->
            val user = when (authState) {
                is AuthState.Authenticated -> authState.user
                is AuthState.Unauthenticated -> null
            }

            HomeScreenState(
                currentUser = user,
                releasesFeed = feed,
                scheduleWeek = schedule,
                currentBestType = currentBestType,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = HomeScreenState()
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onConfirmAction)
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun updateBestType(bestType: BestType) {
        savedStateHandle[BEST_TYPE_KEY] = bestType
    }

    private fun showErrorMessage(error: Throwable, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = error.localizedMessage(),
            action = MessageAction(
                title = UiText.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }

    companion object {
        private const val BEST_TYPE_KEY = "best_type"
    }
}

@Stable
internal data class HomeScreenState(
    val currentUser: User? = null,
    val releasesFeed: ReleasesFeed = ReleasesFeed.create(),
    val scheduleWeek: ScheduleWeek = ScheduleWeek.create(),
    val currentBestType: BestType = BestType.Now,
)

@Stable
internal sealed interface HomeScreenAction {
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
internal enum class BestType {
    Now, AllTime
}
