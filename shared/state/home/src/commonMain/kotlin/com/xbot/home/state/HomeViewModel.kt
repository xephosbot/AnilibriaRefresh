package com.xbot.home.state

import androidx.compose.runtime.Stable
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel(binds = [ViewModel::class])
class HomeViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getAuthState: GetAuthStateUseCase,
    private val getReleasesFeed: GetReleasesFeedUseCase,
    private val getSortedScheduleWeekUseCase: GetSortedScheduleWeekUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<HomeScreenState, HomeScreenSideEffect> {

    override val container: Container<HomeScreenState, HomeScreenSideEffect> = container(
        initialState = HomeScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = HomeScreenState.serializer(),
    ) {
        startLoadData()
    }

    private val pager: Pager<Int, Release> = getCatalogReleasesPager(null, null)

    //TODO: After Paging 3.5.0 released we can use asState extension and move list to HomeScreenState
    val releases: Flow<PagingData<Release>> = pager.flow
        .cachedIn(viewModelScope)

    private var loadDataJob: Job? = null

    private fun startLoadData() {
        loadDataJob?.cancel()
        loadDataJob = intent {
            combine(
                getAuthState(),
                getReleasesFeed(),
                getSortedScheduleWeekUseCase()
            ) { auth, feed, schedule ->
                val user = (auth as? AuthState.Authenticated)?.user

                reduce {
                    state.copy(
                        currentUser = user,
                        releasesFeed = feed,
                        scheduleWeek = schedule
                    )
                }
            }.catch { error ->
                postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error) {
                    onAction(
                        HomeScreenAction.Refresh
                    )
                })
            }.collect()
        }
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Refresh -> refresh()
            is HomeScreenAction.UpdateBestType -> updateBestType(action.bestType)
            is HomeScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
        }
    }

    private fun refresh(): Job = intent {
        //pager.refresh()
        startLoadData()
    }

    private fun updateBestType(bestType: BestType): Job = intent {
        reduce { state.copy(currentBestType = bestType) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(HomeScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}

@Serializable
@Stable
data class HomeScreenState(
    @Transient val currentUser: User? = null,
    @Transient val releasesFeed: ReleasesFeed = ReleasesFeed(),
    @Transient val scheduleWeek: ScheduleWeek = ScheduleWeek(),
    val currentBestType: BestType = BestType.Now,
)

@Stable
sealed interface HomeScreenSideEffect {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit
    ) : HomeScreenSideEffect
}

@Stable
sealed interface HomeScreenAction {
    @Stable
    data object Refresh : HomeScreenAction

    @Stable
    data class UpdateBestType(val bestType: BestType) : HomeScreenAction

    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : HomeScreenAction
}

@Serializable
@Stable
enum class BestType {
    Now, AllTime
}
