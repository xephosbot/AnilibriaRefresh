package com.xbot.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.common.asyncLoad
import com.xbot.common.getOrElse
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.usecase.GetCatalogAgeRatingsUseCase
import com.xbot.domain.usecase.GetCatalogGenresUseCase
import com.xbot.domain.usecase.GetCatalogProductionStatusesUseCase
import com.xbot.domain.usecase.GetCatalogPublishStatusesUseCase
import com.xbot.domain.usecase.GetCatalogReleaseTypesUseCase
import com.xbot.domain.usecase.GetCatalogReleasesUseCase
import com.xbot.domain.usecase.GetCatalogSeasonsUseCase
import com.xbot.domain.usecase.GetCatalogSortingTypesUseCase
import com.xbot.domain.usecase.GetCatalogYearsUseCase
import com.xbot.domain.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class SearchViewModel(
    private val getCatalogReleases: GetCatalogReleasesUseCase,
    private val getCatalogAgeRatings: GetCatalogAgeRatingsUseCase,
    private val getCatalogGenres: GetCatalogGenresUseCase,
    private val getCatalogProductionStatuses: GetCatalogProductionStatusesUseCase,
    private val getCatalogPublishStatuses: GetCatalogPublishStatusesUseCase,
    private val getCatalogReleaseTypes: GetCatalogReleaseTypesUseCase,
    private val getCatalogSeasons: GetCatalogSeasonsUseCase,
    private val getCatalogSortingTypes: GetCatalogSortingTypesUseCase,
    private val getCatalogYears: GetCatalogYearsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(),
    OrbitContainerHost<SearchScreenState, SearchScreenState, SearchScreenSideEffect> {

    override val container:
        OrbitContainer<SearchScreenState, SearchScreenState, SearchScreenSideEffect> =
        orbitContainer(
            initialState = SearchScreenState(),
            savedStateHandle = savedStateHandle,
            serializer = SearchScreenState.serializer()
        ) {
            coroutineScope {
                launch { loadGenres() }
                launch { loadReleaseTypes() }
                launch { loadPublishStatuses() }
                launch { loadProductionStatuses() }
                launch { loadSortingTypes() }
                launch { loadSeasons() }
                launch { loadAgeRatings() }
                launch { loadYears() }
            }
        }

    // TODO: Move inside SearchScreenState once Paging 3.5.0 stable ships asState()
    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        container.stateFlow.map { it.query }.distinctUntilChanged().debounce(500L),
        container.stateFlow.map { it.filters }.distinctUntilChanged()
    ) { query, filters ->
        query to filters
    }.flatMapLatest { (query, filters) ->
        getCatalogReleases(
            GetCatalogReleasesUseCase.Params(
                search = query,
                filters = filters.takeIf { it.hasActiveFilters }?.toCatalogQuery()
            )
        )
    }.cachedIn(viewModelScope)

    private suspend fun loadGenres() = subIntent {
        asyncLoad(
            request = { getCatalogGenres() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(genres = it)
            }
        )
    }

    private suspend fun loadReleaseTypes() = subIntent {
        asyncLoad(
            request = { getCatalogReleaseTypes() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releaseTypes = it)
            }
        )
    }

    private suspend fun loadPublishStatuses() = subIntent {
        asyncLoad(
            request = { getCatalogPublishStatuses() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(publishStatuses = it)
            }
        )
    }

    private suspend fun loadProductionStatuses() = subIntent {
        asyncLoad(
            request = { getCatalogProductionStatuses() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(productionStatuses = it)
            }
        )
    }

    private suspend fun loadSortingTypes() = subIntent {
        asyncLoad(
            request = { getCatalogSortingTypes() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(sortingTypes = it)
            }
        )
    }

    private suspend fun loadSeasons() = subIntent {
        asyncLoad(
            request = { getCatalogSeasons() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(seasons = it)
            }
        )
    }

    private suspend fun loadAgeRatings() = subIntent {
        asyncLoad(
            request = { getCatalogAgeRatings() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(ageRatings = it)
            }
        )
    }

    private suspend fun loadYears() = subIntent {
        asyncLoad(
            request = { getCatalogYears() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(
                    years = it,
                    filters = state.filters.copy(selectedYears = it.getOrElse { IntRange.EMPTY })
                )
            }
        )
    }

    fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.QueryChanged -> updateQuery(action.query)

            is SearchScreenAction.ToggleGenre -> toggleGenre(action.genre)

            is SearchScreenAction.ToggleProductionStatus ->
                toggleProductionStatus(action.productionStatus)

            is SearchScreenAction.TogglePublishStatus -> togglePublishStatus(action.publishStatus)

            is SearchScreenAction.ToggleReleaseType -> toggleReleaseType(action.releaseType)

            is SearchScreenAction.ToggleSeason -> toggleSeason(action.season)

            is SearchScreenAction.UpdateSortingType -> updateSortingType(action.sortingType)

            is SearchScreenAction.UpdateYearsRange -> updateYearsRange(action.years)

            is SearchScreenAction.ToggleAgeRating -> toggleAgeRating(action.ageRating)

            is SearchScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)

            is SearchScreenAction.Refresh -> refresh()
        }
    }

    private fun updateQuery(query: String) = intent {
        reduce { state.copy(query = query) }
    }

    private fun updateFilters(transform: SearchFiltersState.() -> SearchFiltersState) = intent {
        reduce { state.copy(filters = state.filters.transform()) }
    }

    private fun toggleGenre(genre: Genre) = updateFilters {
        copy(selectedGenres = selectedGenres.toggle(genre))
    }

    private fun toggleProductionStatus(productionStatus: ProductionStatus) = updateFilters {
        copy(selectedProductionStatuses = selectedProductionStatuses.toggle(productionStatus))
    }

    private fun togglePublishStatus(publishStatus: PublishStatus) = updateFilters {
        copy(selectedPublishStatuses = selectedPublishStatuses.toggle(publishStatus))
    }

    private fun toggleReleaseType(releaseType: ReleaseType) = updateFilters {
        copy(selectedReleaseTypes = selectedReleaseTypes.toggle(releaseType))
    }

    private fun toggleSeason(season: Season) = updateFilters {
        copy(selectedSeasons = selectedSeasons.toggle(season))
    }

    private fun updateSortingType(sortingType: SortingType) = updateFilters {
        copy(selectedSortingType = sortingType)
    }

    private fun updateYearsRange(years: IntRange) = updateFilters {
        copy(selectedYears = years)
    }

    private fun toggleAgeRating(ageRating: AgeRating) = updateFilters {
        copy(selectedAgeRatings = selectedAgeRatings.toggle(ageRating))
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit) = intent {
        postSideEffect(SearchScreenSideEffect.ShowErrorMessage(error, onRetry))
    }

    private fun refresh(): Job = intent {
        coroutineScope {
            launch { loadGenres() }
            launch { loadReleaseTypes() }
            launch { loadPublishStatuses() }
            launch { loadProductionStatuses() }
            launch { loadSortingTypes() }
            launch { loadSeasons() }
            launch { loadAgeRatings() }
            launch { loadYears() }
        }
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item
}
