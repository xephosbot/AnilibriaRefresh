package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Franchise
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.Schedule
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetCatalogReleasesPager
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
    getCatalogReleasesPager: GetCatalogReleasesPager,
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getCatalogReleasesPager()
        .cachedIn(viewModelScope)

    private val releasesFeed: MutableStateFlow<ReleasesFeed?> = MutableStateFlow(null)
    private val currentBestType: MutableStateFlow<BestType> =
        MutableStateFlow(BestType.Now)

    val state: StateFlow<FeedScreenState> =
        combine(releasesFeed, currentBestType) { releasesFeed, bestType ->
            if (releasesFeed != null) {
                FeedScreenState.Success(
                    recommendedReleases = releasesFeed.recommendedReleases,
                    scheduleNow = releasesFeed.scheduleNow,
                    bestReleases = if (bestType == BestType.Now) releasesFeed.bestNow else releasesFeed.bestAllTime,
                    recommendedFranchises = releasesFeed.recommendedFranchises,
                    genres = releasesFeed.genres,
                    currentBestType = bestType,
                )
            } else {
                FeedScreenState.Loading
            }
        }
            .onStart { refresh() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = FeedScreenState.Loading
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
            releasesFeed.update { null }
            getReleasesFeed()
                .onRight { feed ->
                    releasesFeed.update { feed }
                }
                .onLeft {
                    showErrorMessage(it.toString(), ::refresh)
                }
        }
    }

    private fun updateBestType(bestType: BestType) {
        viewModelScope.launch {
            currentBestType.update { bestType }
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
sealed interface FeedScreenState {
    @Stable
    data object Loading : FeedScreenState
    @Stable
    data class Success(
        val recommendedReleases: List<Release>,
        val scheduleNow: List<Schedule>,
        val bestReleases: List<Release>,
        val recommendedFranchises: List<Franchise>,
        val genres: List<Genre>,
        val currentBestType: BestType,
    ) : FeedScreenState
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