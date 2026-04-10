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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogAgeRatings: GetCatalogAgeRatingsUseCase,
    private val getCatalogGenres: GetCatalogGenresUseCase,
    private val getCatalogProductionStatuses: GetCatalogProductionStatusesUseCase,
    private val getCatalogPublishStatuses: GetCatalogPublishStatusesUseCase,
    private val getCatalogReleaseTypes: GetCatalogReleaseTypesUseCase,
    private val getCatalogSeasons: GetCatalogSeasonsUseCase,
    private val getCatalogSortingTypes: GetCatalogSortingTypesUseCase,
    private val getCatalogYears: GetCatalogYearsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<SearchScreenState, SearchScreenSideEffect> {

    override val container: Container<SearchScreenState, SearchScreenSideEffect> = container(
        initialState = SearchScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = SearchScreenState.serializer()
    ) {
        loadGenres()
        loadReleaseTypes()
        loadPublishStatuses()
        loadProductionStatuses()
        loadSortingTypes()
        loadSeasons()
        loadAgeRatings()
        loadYears()
    }

    // TODO: Move inside SearchScreenState once Paging 3.5.0 stable ships asState()
    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        container.stateFlow.map { it.query }.distinctUntilChanged().debounce(500L),
        container.stateFlow.map { it.filters }.distinctUntilChanged(),
    ) { query, filters ->
        query to filters
    }.flatMapLatest { (query, filters) ->
        getCatalogReleasesPager(
            search = query,
            filters = filters.takeIf { it.hasActiveFilters }?.toCatalogQuery(),
        ).flow
    }.cachedIn(viewModelScope)

    @OptIn(OrbitExperimental::class)
    private fun loadGenres(): Job = intent {
        asyncLoad(
            request = { getCatalogGenres() },
            onError = { error -> showErrorMessage(error) { loadGenres() } },
            reducer = {
                copy(genres = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadReleaseTypes(): Job = intent {
        asyncLoad(
            request = { getCatalogReleaseTypes() },
            onError = { error -> showErrorMessage(error) { loadReleaseTypes() } },
            reducer = {
                copy(releaseTypes = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadPublishStatuses(): Job = intent {
        asyncLoad(
            request = { getCatalogPublishStatuses() },
            onError = { error -> showErrorMessage(error) { loadPublishStatuses() } },
            reducer = {
                copy(publishStatuses = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadProductionStatuses(): Job = intent {
        asyncLoad(
            request = { getCatalogProductionStatuses() },
            onError = { error -> showErrorMessage(error) { loadProductionStatuses() } },
            reducer = {
                copy(productionStatuses = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadSortingTypes(): Job = intent {
        asyncLoad(
            request = { getCatalogSortingTypes() },
            onError = { error -> showErrorMessage(error) { loadSortingTypes() } },
            reducer = {
                copy(sortingTypes = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadSeasons(): Job = intent {
        asyncLoad(
            request = { getCatalogSeasons() },
            onError = { error -> showErrorMessage(error) { loadSeasons() } },
            reducer = {
                copy(seasons = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadAgeRatings(): Job = intent {
        asyncLoad(
            request = { getCatalogAgeRatings() },
            onError = { error -> showErrorMessage(error) { loadAgeRatings() } },
            reducer = {
                copy(ageRatings = it)
            }
        )
    }

    @OptIn(OrbitExperimental::class)
    private fun loadYears(): Job = intent {
        asyncLoad(
            request = { getCatalogYears() },
            onError = { error -> showErrorMessage(error) { loadYears() } },
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

    private fun refresh() {
        loadGenres()
        loadReleaseTypes()
        loadPublishStatuses()
        loadProductionStatuses()
        loadSortingTypes()
        loadSeasons()
        loadAgeRatings()
        loadYears()
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item
}
