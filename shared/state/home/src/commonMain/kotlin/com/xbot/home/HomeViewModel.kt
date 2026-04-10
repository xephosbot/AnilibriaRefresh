package com.xbot.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.common.asyncLoad
import com.xbot.domain.models.AuthState
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
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<HomeScreenState, HomeScreenSideEffect> {

    override val container: Container<HomeScreenState, HomeScreenSideEffect> = container(
        initialState = HomeScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = HomeScreenState.serializer(),
    ) {
        loadBestReleasesInCurrentSeason()
        loadBestReleasesForAllTime()
        loadRecommendedFranchises()
        loadRecommendedReleases()
        loadRecommendedGenres()
        loadScheduleForToday()
        loadScheduleWeek()
        loadAuthState()
    }

    private val pager: Pager<Int, Release> = getCatalogReleasesPager(null, null)

    private var authStateJob: Job? = null

    // TODO: Move inside HomeScreenState once Paging 3.5.0 stable ships asState()
    val releases: Flow<PagingData<Release>> = pager.flow.cachedIn(viewModelScope)

    private fun loadBestReleasesInCurrentSeason(): Job = intent {
        asyncLoad(
            request = { getBestReleasesInCurrentSeason() },
            onError = { error -> showErrorMessage(error) { loadBestReleasesInCurrentSeason() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestNow = it))
            }
        )
    }

    private fun loadBestReleasesForAllTime(): Job = intent {
        asyncLoad(
            request = { getBestReleasesForAllTime() },
            onError = { error -> showErrorMessage(error) { loadBestReleasesForAllTime() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(bestAllTime = it))
            }
        )
    }

    private fun loadRecommendedFranchises(): Job = intent {
        asyncLoad(
            request = { getRecommendedFranchisesUseCase() },
            onError = { error -> showErrorMessage(error) { loadRecommendedFranchises() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedFranchises = it))
            }
        )
    }

    private fun loadRecommendedReleases(): Job = intent {
        asyncLoad(
            request = { getRecommendedReleases() },
            onError = { error -> showErrorMessage(error) { loadRecommendedReleases() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(recommendedReleases = it))
            }
        )
    }

    private fun loadRecommendedGenres(): Job = intent {
        asyncLoad(
            request = { getRecommendedGenres() },
            onError = { error -> showErrorMessage(error) { loadRecommendedGenres() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(genres = it))
            }
        )
    }

    private fun loadScheduleForToday(): Job = intent {
        asyncLoad(
            request = { getScheduleForToday() },
            onError = { error -> showErrorMessage(error) { loadScheduleForToday() } },
            reducer = {
                copy(releasesFeed = state.releasesFeed.copy(scheduleNow = it))
            }
        )
    }

    private fun loadScheduleWeek(): Job = intent {
        asyncLoad(
            request = { getScheduleWeek() },
            onError = { error -> showErrorMessage(error) { loadScheduleWeek() } },
            reducer = {
                copy(scheduleWeek = state.scheduleWeek.copy(days = it))
            }
        )
    }

    private fun loadAuthState() {
        authStateJob?.cancel()
        authStateJob = intent {
            getAuthState().collect { authState ->
                reduce {
                    state.copy(currentUser = (authState as? AuthState.Authenticated)?.user)
                }
            }
        }
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
        }
    }

    private fun refresh() {
        loadBestReleasesInCurrentSeason()
        loadBestReleasesForAllTime()
        loadRecommendedFranchises()
        loadRecommendedReleases()
        loadRecommendedGenres()
        loadScheduleForToday()
        loadScheduleWeek()
        loadAuthState()
    }

    private fun updateBestType(bestType: BestType): Job = intent {
        reduce { state.copy(currentBestType = bestType) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}
