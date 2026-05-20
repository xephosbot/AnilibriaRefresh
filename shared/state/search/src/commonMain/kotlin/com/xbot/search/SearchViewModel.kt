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
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetCatalogSeasonsUseCase
import com.xbot.domain.usecase.GetCatalogSortingTypesUseCase
import com.xbot.domain.usecase.GetCatalogYearsUseCase
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
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(ExperimentalCoroutinesApi::class, OrbitExperimental::class)
@KoinViewModel
class SearchViewModel(
    private val getCatalogReleasesPager: Lazy<GetCatalogReleasesPagerUseCase>,
    private val getCatalogAgeRatings: Lazy<GetCatalogAgeRatingsUseCase>,
    private val getCatalogGenres: Lazy<GetCatalogGenresUseCase>,
    private val getCatalogProductionStatuses: Lazy<GetCatalogProductionStatusesUseCase>,
    private val getCatalogPublishStatuses: Lazy<GetCatalogPublishStatusesUseCase>,
    private val getCatalogReleaseTypes: Lazy<GetCatalogReleaseTypesUseCase>,
    private val getCatalogSeasons: Lazy<GetCatalogSeasonsUseCase>,
    private val getCatalogSortingTypes: Lazy<GetCatalogSortingTypesUseCase>,
    private val getCatalogYears: Lazy<GetCatalogYearsUseCase>,
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel(), ContainerHost<SearchScreenState, SearchScreenSideEffect> {

    override val container: Container<SearchScreenState, SearchScreenSideEffect> = container(
        initialState = SearchScreenState(),
        savedStateHandle = savedStateHandle ?: SavedStateHandle(),
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
        container.stateFlow.map { it.filters }.distinctUntilChanged(),
    ) { query, filters ->
        query to filters
    }.flatMapLatest { (query, filters) ->
        getCatalogReleasesPager.value(
            search = query,
            filters = filters.takeIf { it.hasActiveFilters }?.toCatalogQuery(),
        ).flow
    }.cachedIn(viewModelScope)

    private suspend fun loadGenres() = subIntent {
        asyncLoad(
            request = { getCatalogGenres.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(genres = it)
            }
        )
    }

    private suspend fun loadReleaseTypes() = subIntent {
        asyncLoad(
            request = { getCatalogReleaseTypes.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(releaseTypes = it)
            }
        )
    }

    private suspend fun loadPublishStatuses() = subIntent {
        asyncLoad(
            request = { getCatalogPublishStatuses.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(publishStatuses = it)
            }
        )
    }

    private suspend fun loadProductionStatuses() = subIntent {
        asyncLoad(
            request = { getCatalogProductionStatuses.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(productionStatuses = it)
            }
        )
    }

    private suspend fun loadSortingTypes() = subIntent {
        asyncLoad(
            request = { getCatalogSortingTypes.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(sortingTypes = it)
            }
        )
    }

    private suspend fun loadSeasons() = subIntent {
        asyncLoad(
            request = { getCatalogSeasons.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(seasons = it)
            }
        )
    }

    private suspend fun loadAgeRatings() = subIntent {
        asyncLoad(
            request = { getCatalogAgeRatings.value() },
            onError = { error -> showErrorMessage(error) { refresh() } },
            reducer = {
                copy(ageRatings = it)
            }
        )
    }

    private suspend fun loadYears() = subIntent {
        asyncLoad(
            request = { getCatalogYears.value() },
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
            is SearchScreenAction.ToggleProductionStatus -> toggleProductionStatus(action.productionStatus)
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

    private fun toggleGenre(genre: Genre) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedGenres = state.filters.selectedGenres.toggle(genre))) }
    }

    private fun toggleProductionStatus(productionStatus: ProductionStatus) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedProductionStatuses = state.filters.selectedProductionStatuses.toggle(productionStatus))) }
    }

    private fun togglePublishStatus(publishStatus: PublishStatus) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedPublishStatuses = state.filters.selectedPublishStatuses.toggle(publishStatus))) }
    }

    private fun toggleReleaseType(releaseType: ReleaseType) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedReleaseTypes = state.filters.selectedReleaseTypes.toggle(releaseType))) }
    }

    private fun toggleSeason(season: Season) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedSeasons = state.filters.selectedSeasons.toggle(season))) }
    }

    private fun updateSortingType(sortingType: SortingType) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedSortingType = sortingType)) }
    }

    private fun updateYearsRange(years: IntRange) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedYears = years)) }
    }

    private fun toggleAgeRating(ageRating: AgeRating) = intent {
        reduce { state.copy(filters = state.filters.copy(selectedAgeRatings = state.filters.selectedAgeRatings.toggle(ageRating))) }
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
