package com.xbot.title

import androidx.lifecycle.ViewModel
import com.xbot.common.asyncLoad
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.CollectionType
import com.xbot.domain.usecase.GetFranchiseReleasesUseCase
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

@KoinViewModel
class TitleViewModel(
    @Provided private val aliasOrId: String,
    @Provided private val initialRelease: Release? = null,
    private val getRelease: GetReleaseUseCase,
    private val getFranchiseReleases: GetFranchiseReleasesUseCase
) : ViewModel(),
    OrbitContainerHost<TitleScreenState, TitleScreenState, TitleScreenSideEffect> {

    override val container:
        OrbitContainer<TitleScreenState, TitleScreenState, TitleScreenSideEffect> = orbitContainer(
            initialState = TitleScreenState(initialRelease = initialRelease)
        ) {
            loadAll()
        }

    fun onAction(action: TitleScreenAction) {
        when (action) {
            is TitleScreenAction.Refresh -> refresh()

            is TitleScreenAction.OnTabSelect -> onTabSelect(action.tab)

            is TitleScreenAction.OnEpisodesSortChange -> onEpisodesSortChange(action.sort)

            is TitleScreenAction.OnFavoriteToggle -> onFavoriteToggle()

            is TitleScreenAction.OnCollectionStatusSelect ->
                onCollectionStatusSelect(action.status)
        }
    }

    private fun refresh(): Job = intent {
        loadAll()
    }

    private fun onTabSelect(tab: ReleaseTab): Job = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun onEpisodesSortChange(sort: EpisodesSort): Job = intent {
        reduce { state.copy(episodesSort = sort) }
    }

    private fun onFavoriteToggle(): Job = intent {
        reduce { state.copy(isFavorite = !state.isFavorite) }
    }

    private fun onCollectionStatusSelect(status: CollectionType?): Job = intent {
        reduce { state.copy(collectionStatus = status) }
    }

    private suspend fun loadAll() = coroutineScope {
        launch { loadDetails() }
        launch { loadFranchiseReleases() }
    }

    private suspend fun loadDetails() = subIntent {
        asyncLoad(
            request = { getRelease(GetReleaseUseCase.Params(aliasOrId)) },
            onError = { error -> showError(error) },
            reducer = { copy(details = it) }
        )
    }

    private suspend fun loadFranchiseReleases() = subIntent {
        asyncLoad(
            request = { getFranchiseReleases(GetFranchiseReleasesUseCase.Params(aliasOrId)) },
            onError = { error -> showError(error) },
            reducer = { copy(franchiseReleases = it) }
        )
    }

    private fun showError(error: Throwable): Job = intent {
        postSideEffect(TitleScreenSideEffect.ShowErrorMessage(error) { refresh() })
    }
}
