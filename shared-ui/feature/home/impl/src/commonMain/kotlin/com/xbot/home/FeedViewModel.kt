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

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getCatalogReleasesPager()
        .cachedIn(viewModelScope)

    private val refreshTrigger = MutableStateFlow(0)
    private val bestType = MutableStateFlow(BestType.Now)

    private val feedData = refreshTrigger
        .flatMapLatest {
            flow {
                when (val result = getReleasesFeed()) {
                    is Either.Left -> {
                        showErrorMessage(result.value.toString()) { refresh() }
                        emit(null)
                    }
                    is Either.Right -> emit(result.value)
                }
            }
        }

    val state: StateFlow<FeedScreenState> =
        combine(getAuthState(), feedData, bestType) { authState, feed, currentBestType ->
            val user = when (authState) {
                is AuthState.Authenticated -> authState.user
                is AuthState.Unauthenticated -> null
            }

            FeedScreenState(
                isLoading = feed == null,
                currentUser = user,
                recommendedReleases = feed?.recommendedReleases.orEmpty(),
                scheduleNow = feed?.scheduleNow.orEmpty(),
                bestNow = feed?.bestNow.orEmpty(),
                bestAllTime = feed?.bestAllTime.orEmpty(),
                recommendedFranchises = feed?.recommendedFranchises.orEmpty(),
                genres = feed?.genres.orEmpty(),
                currentBestType = currentBestType,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeedScreenState()
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