package com.xbot.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.ScheduleWeek
import com.xbot.domain.models.User
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val releases: Flow<PagingData<Release>> = refreshTrigger.flatMapLatest {
        getCatalogReleasesPager(null, null).flow
    }.cachedIn(viewModelScope)

    private val bestType = savedStateHandle.getStateFlow(BEST_TYPE_KEY, BestType.Now)

    private val _sideEffects = Channel<HomeScreenSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val feedData = refreshTrigger.flatMapLatest {
        getReleasesFeed().catch { _sideEffects.trySend(HomeScreenSideEffect.ShowError(it) { refresh() }) }
    }

    private val scheduleData = refreshTrigger.flatMapLatest {
        getSortedScheduleWeekUseCase().catch { _sideEffects.trySend(HomeScreenSideEffect.ShowError(it) { refresh() }) }
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

    companion object {
        private const val BEST_TYPE_KEY = "best_type"
    }
}

data class HomeScreenState(
    val currentUser: User? = null,
    val releasesFeed: ReleasesFeed = ReleasesFeed.create(),
    val scheduleWeek: ScheduleWeek = ScheduleWeek.create(),
    val currentBestType: BestType = BestType.Now,
)

sealed interface HomeScreenAction {
    data object Refresh : HomeScreenAction
    data class UpdateBestType(val bestType: BestType) : HomeScreenAction
}

sealed interface HomeScreenSideEffect {
    data class ShowError(val error: Throwable, val retryAction: () -> Unit) : HomeScreenSideEffect
}

enum class BestType {
    Now, AllTime
}
