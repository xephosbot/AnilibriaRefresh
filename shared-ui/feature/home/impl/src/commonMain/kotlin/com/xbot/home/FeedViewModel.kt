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
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.Schedule
import com.xbot.domain.models.User
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getCatalogReleasesPager()
        .cachedIn(viewModelScope)

    private val _state: MutableStateFlow<FeedScreenState> = MutableStateFlow(FeedScreenState())
    val state: StateFlow<FeedScreenState> =
        combine(_state, getAuthState()) { state, authState ->
            val user = when (authState) {
                is AuthState.Authenticated -> authState.user
                is AuthState.Unauthenticated -> null
            }
            state.copy(currentUser = user)
        }
            .onStart { refresh() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = _state.value
            )

    fun onAction(action: FeedScreenAction) {
        when (action) {
            is FeedScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }

            is FeedScreenAction.Refresh -> refresh()

            is FeedScreenAction.UpdateBestType -> updateBestType(action.bestType)
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getReleasesFeed()) {
                is Either.Left -> showErrorMessage(result.value.toString(), ::refresh)
                is Either.Right -> _state.update {
                    it.copy(
                        isLoading = false,
                        recommendedReleases = result.value.recommendedReleases,
                        scheduleNow = result.value.scheduleNow,
                        bestNow = result.value.bestNow,
                        bestAllTime = result.value.bestAllTime,
                        recommendedFranchises = result.value.recommendedFranchises,
                        genres = result.value.genres,
                    )
                }
            }
        }
    }

    private fun updateBestType(bestType: BestType) {
        viewModelScope.launch {
            _state.update {
                it.copy(currentBestType = bestType)
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
data class FeedScreenState(
    val isLoading: Boolean = true,
    val currentUser: User? = null,
    val recommendedReleases: List<Release> = emptyList(),
    val scheduleNow: List<Schedule> = emptyList(),
    val bestNow: List<Release> = emptyList(),
    val bestAllTime: List<Release> = emptyList(),
    val recommendedFranchises: List<Franchise> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val currentBestType: BestType = BestType.Now,
) {
    val bestReleases: List<Release>
        get() = if (currentBestType == BestType.Now) bestNow else bestAllTime
}

@Stable
sealed interface FeedScreenAction {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : FeedScreenAction
    @Stable
    data object Refresh : FeedScreenAction
    @Stable
    data class UpdateBestType(val bestType: BestType) : FeedScreenAction
}

@Stable
enum class BestType {
    Now, AllTime
}