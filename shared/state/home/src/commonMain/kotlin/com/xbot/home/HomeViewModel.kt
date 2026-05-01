package com.xbot.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.common.asyncLoad
import com.xbot.domain.models.AuthState
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetBestReleasesForAllTimeUseCase
import com.xbot.domain.usecase.GetBestReleasesInCurrentSeasonUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetRecommendedFranchisesUseCase
import com.xbot.domain.usecase.GetRecommendedGenresUseCase
import com.xbot.domain.usecase.GetRecommendedReleasesUseCase
import com.xbot.domain.usecase.GetScheduleForTodayUseCase
import com.xbot.domain.usecase.GetScheduleWeekUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

private typealias ErrorHandler = suspend (DomainError) -> Unit
private typealias DataLoader = suspend (onError: ErrorHandler) -> Unit

@OptIn(OrbitExperimental::class)
@KoinViewModel
class HomeViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getAuthState: GetAuthStateUseCase,
    private val getBestReleasesForAllTime: GetBestReleasesForAllTimeUseCase,
    private val getBestReleasesInCurrentSeason: GetBestReleasesInCurrentSeasonUseCase,
    private val getRecommendedFranchisesUseCase: GetRecommendedFranchisesUseCase,
    private val getRecommendedReleases: GetRecommendedReleasesUseCase,
    private val getRecommendedGenres: GetRecommendedGenresUseCase,
    private val getScheduleForToday: GetScheduleForTodayUseCase,
    private val getScheduleWeek: GetScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel(), ContainerHost<HomeScreenState, HomeScreenSideEffect> {

    override val container: Container<HomeScreenState, HomeScreenSideEffect> = container(
        initialState = HomeScreenState(),
        savedStateHandle = savedStateHandle ?: SavedStateHandle(),
        serializer = HomeScreenState.serializer(),
    ) {
        refresh()
    }

    private val pager: Pager<Int, Release> = getCatalogReleasesPager(null, null)

    // TODO: Move inside HomeScreenState once Paging 3.5.0 stable ships asState()
    val releases: Flow<PagingData<Release>> = pager.flow.cachedIn(viewModelScope)

    private suspend fun loadBestReleasesInCurrentSeason(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getBestReleasesInCurrentSeason() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestNow = it))
            }
        )
    }

    private suspend fun loadBestReleasesForAllTime(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getBestReleasesForAllTime() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestAllTime = it))
            }
        )
    }

    private suspend fun loadRecommendedFranchises(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getRecommendedFranchisesUseCase() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedFranchises = it))
            }
        )
    }

    private suspend fun loadRecommendedReleases(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getRecommendedReleases() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedReleases = it))
            }
        )
    }

    private suspend fun loadRecommendedGenres(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getRecommendedGenres() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(genres = it))
            }
        )
    }

    private suspend fun loadScheduleForToday(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getScheduleForToday() },
            onError = { error -> onError(error) },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(scheduleNow = it))
            }
        )
    }

    private suspend fun loadScheduleWeek(onError: ErrorHandler) = subIntent {
        asyncLoad(
            request = { getScheduleWeek() },
            onError = { error -> onError(error) },
            reducer = {
                copy(scheduleWeek = state.scheduleWeek.copy(days = it))
            }
        )
    }

    private suspend fun loadAuthState(onError: ErrorHandler) = subIntent {
        val authFlow = getAuthState()
        try {
            val first = authFlow.first()
            reduce {
                state.copy(currentUser = (first as? AuthState.Authenticated)?.user)
            }
            viewModelScope.launch {
                authFlow.drop(1).collect { authState ->
                    reduce {
                        state.copy(currentUser = (authState as? AuthState.Authenticated)?.user)
                    }
                }
            }
        } catch (e: Exception) {
            val domainError = e as? DomainError ?: DomainError.UnknownError(e)
            onError(domainError)
        }
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(
                error = action.error as? DomainError ?: DomainError.UnknownError(action.error),
                onRetry = action.onRetry,
            )
        }
    }

    private fun refresh(): Job = performBatchLoad(getAllLoaders())

    private fun getAllLoaders() = listOf<DataLoader>(
        { loadBestReleasesInCurrentSeason(it) },
        { loadBestReleasesForAllTime(it) },
        { loadRecommendedFranchises(it) },
        { loadRecommendedReleases(it) },
        { loadRecommendedGenres(it) },
        { loadScheduleForToday(it) },
        { loadScheduleWeek(it) },
        { loadAuthState(it) }
    )

    private fun performBatchLoad(loaders: List<DataLoader>): Job = intent {
        val failedLoaders = mutableListOf<DataLoader>()
        var firstError: DomainError? = null
        val mutex = Mutex()

        coroutineScope {
            loaders.forEach { loader ->
                launch {
                    loader { error ->
                        mutex.withLock {
                            if (firstError == null) firstError = error
                            failedLoaders.add(loader)
                        }
                    }
                }
            }
        }

        firstError?.let { error ->
            showErrorMessage(error) {
                performBatchLoad(failedLoaders)
            }
        }
    }

    private fun updateBestType(bestType: BestType): Job = intent {
        reduce { state.copy(currentBestType = bestType) }
    }

    private fun showErrorMessage(error: DomainError, onRetry: () -> Unit): Job = intent {
        postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}
