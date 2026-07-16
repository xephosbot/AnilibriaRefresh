package com.xbot.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.common.asyncLoad
import com.xbot.domain.models.Release
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
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

@KoinViewModel
class HomeViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getBestReleasesForAllTime: GetBestReleasesForAllTimeUseCase,
    private val getBestReleasesInCurrentSeason: GetBestReleasesInCurrentSeasonUseCase,
    private val getRecommendedFranchisesUseCase: GetRecommendedFranchisesUseCase,
    private val getRecommendedReleases: GetRecommendedReleasesUseCase,
    private val getRecommendedGenres: GetRecommendedGenresUseCase,
    private val getScheduleForToday: GetScheduleForTodayUseCase,
    private val getScheduleWeek: GetScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), OrbitContainerHost<HomeScreenState, HomeScreenState, HomeScreenSideEffect> {

    override val container: OrbitContainer<HomeScreenState, HomeScreenState, HomeScreenSideEffect> = orbitContainer(
        initialState = HomeScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = HomeScreenState.serializer(),
    ) {
        coroutineScope {
            launch { loadBestReleasesInCurrentSeason() }
            launch { loadBestReleasesForAllTime() }
            launch { loadRecommendedFranchises() }
            launch { loadRecommendedReleases() }
            launch { loadRecommendedGenres() }
            launch { loadScheduleForToday() }
            launch { loadScheduleWeek() }
        }
    }

    private val pager: Pager<Int, Release> = getCatalogReleasesPager(null, null)

    // TODO: Move inside HomeScreenState once Paging 3.5.0 stable ships asState()
    val releases: Flow<PagingData<Release>> = pager.flow.cachedIn(viewModelScope)

    private suspend fun loadBestReleasesInCurrentSeason() = subIntent {
        asyncLoad(
            request = { getBestReleasesInCurrentSeason() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestNow = it))
            }
        )
    }

    private suspend fun loadBestReleasesForAllTime() = subIntent {
        asyncLoad(
            request = { getBestReleasesForAllTime() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestAllTime = it))
            }
        )
    }

    private suspend fun loadRecommendedFranchises() = subIntent {
        asyncLoad(
            request = { getRecommendedFranchisesUseCase() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedFranchises = it))
            }
        )
    }

    private suspend fun loadRecommendedReleases() = subIntent {
        asyncLoad(
            request = { getRecommendedReleases() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedReleases = it))
            }
        )
    }

    private suspend fun loadRecommendedGenres() = subIntent {
        asyncLoad(
            request = { getRecommendedGenres() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(genres = it))
            }
        )
    }

    private suspend fun loadScheduleForToday() = subIntent {
        asyncLoad(
            request = { getScheduleForToday() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(scheduleNow = it))
            }
        )
    }

    private suspend fun loadScheduleWeek() = subIntent {
        asyncLoad(
            request = { getScheduleWeek() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(scheduleWeek = state.scheduleWeek.copy(days = it))
            }
        )
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
        }
    }

    private fun refresh(): Job = intent {
        coroutineScope {
            launch { loadBestReleasesInCurrentSeason() }
            launch { loadBestReleasesForAllTime() }
            launch { loadRecommendedFranchises() }
            launch { loadRecommendedReleases() }
            launch { loadRecommendedGenres() }
            launch { loadScheduleForToday() }
            launch { loadScheduleWeek() }
        }
    }

    private fun updateBestType(bestType: BestType): Job = intent {
        reduce { state.copy(currentBestType = bestType) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}
