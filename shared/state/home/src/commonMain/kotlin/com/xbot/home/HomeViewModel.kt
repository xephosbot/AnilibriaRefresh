package com.xbot.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
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
import io.nlopez.asyncresult.AsyncResult
import io.nlopez.asyncresult.Error
import io.nlopez.asyncresult.Loading
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<HomeScreenState, HomeScreenSideEffect> {

    override val container: Container<HomeScreenState, HomeScreenSideEffect> =
        container(
            initialState = HomeScreenState(),
            savedStateHandle = savedStateHandle,
            serializer = HomeScreenState.serializer(),
        ) {
            startLoadData()
        }

    private val pager: Pager<Int, Release> = getCatalogReleasesPager(null, null)

    // TODO: After Paging 3.5.0 use asState() and move inside state
    val releases: Flow<PagingData<Release>> = pager.flow.cachedIn(viewModelScope)

    private var loadDataJob: Job? = null

    private fun startLoadData() {
        loadDataJob?.cancel()
        loadDataJob = intent {
            combine(
                getAuthState(),
                getReleasesFeed(),
                getSortedScheduleWeekUseCase(),
            ) { auth, feed, schedule ->
                val safeFeed = feed.catch { error ->
                    postSideEffect(
                        HomeScreenSideEffect.ShowErrorMessage(error.throwable!!) { onAction(HomeScreenAction.Refresh) }
                    )
                }

                reduce {
                    state.copy(
                        currentUser = (auth as? AuthState.Authenticated)?.user,
                        releasesFeed = safeFeed,
                        scheduleWeek = schedule,
                    )
                }
            }.catch { error ->
                postSideEffect(
                    HomeScreenSideEffect.ShowErrorMessage(error) { onAction(HomeScreenAction.Refresh) }
                )
            }.collect {}
        }
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
        }
    }

    private fun refresh(): Job = intent { startLoadData() }

    private fun updateBestType(bestType: BestType): Job = intent {
        reduce { state.copy(currentBestType = bestType) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error, onRetry))
    }

    private inline fun <T> AsyncResult<T>.errorToLoading(
        onError: (Error) -> Unit = {}
    ): AsyncResult<T> = when (this) {
        is Error -> {
            onError(this)
            Loading
        }
        else -> this
    }

    private inline fun ReleasesFeed.catch(
        onError: (Error) -> Unit = {}
    ): ReleasesFeed {
        return copy(
            recommendedReleases = recommendedReleases.errorToLoading(),
            scheduleNow = scheduleNow.errorToLoading(),
            bestNow = bestNow.errorToLoading(),
            bestAllTime = bestAllTime.errorToLoading(),
            recommendedFranchises = recommendedFranchises.errorToLoading(),
            genres = genres.errorToLoading(),
        )
    }
}

@Serializable
data class HomeScreenState(
    @Transient val currentUser: User? = null,
    @Transient val releasesFeed: ReleasesFeed = ReleasesFeed(),
    @Transient val scheduleWeek: ScheduleWeek = ScheduleWeek.create(),
    val currentBestType: BestType = BestType.Now,
)

sealed interface HomeScreenAction {
    data object Refresh : HomeScreenAction
    data class UpdateBestType(val bestType: BestType) : HomeScreenAction
    //TODO: Remove after Paging 3.5.0 use asState()
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenAction
}

sealed interface HomeScreenSideEffect {
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenSideEffect
}

@Serializable
enum class BestType { Now, AllTime }
