package com.xbot.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.domain.models.Genre
import com.xbot.domain.models.Release
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.usecase.GetCatalogFiltersUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<SearchScreenState, SearchScreenSideEffect> {

    override val container: Container<SearchScreenState, SearchScreenSideEffect> = container(
        initialState = SearchScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = SearchScreenState.serializer()
    ) {

    }

    //TODO: After Paging 3.5.0 released we can use asState extension and move list to SearchScreenState
    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        container.stateFlow.map { it.query }.distinctUntilChanged().debounce(500L),
        container.stateFlow.map { it.toSelectedFilters() }.distinctUntilChanged(),
    ) { query, filters ->
        query to filters
    }.flatMapLatest { (query, filters) ->
        getCatalogReleasesPager(
            search = query,
            filters = filters.takeIf { !it.isEmpty() },
        ).flow
    }.cachedIn(viewModelScope)

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
            is SearchScreenAction.Refresh -> Unit
        }
    }

    private fun updateQuery(query: String) = intent {
        reduce { state.copy(query = query) }
    }

    private fun toggleGenre(genre: Genre) = intent {
        reduce { state.copy(selectedGenres = state.selectedGenres.toggle(genre)) }
    }

    private fun toggleProductionStatus(productionStatus: ProductionStatus) = intent {
        reduce { state.copy(selectedProductionStatuses = state.selectedProductionStatuses.toggle(productionStatus)) }
    }

    private fun togglePublishStatus(publishStatus: PublishStatus) = intent {
        reduce { state.copy(selectedPublishStatuses = state.selectedPublishStatuses.toggle(publishStatus)) }
    }

    private fun toggleReleaseType(releaseType: ReleaseType) = intent {
        reduce { state.copy(selectedReleaseTypes = state.selectedReleaseTypes.toggle(releaseType)) }
    }

    private fun toggleSeason(season: Season) = intent {
        reduce { state.copy(selectedSeasons = state.selectedSeasons.toggle(season)) }
    }

    private fun updateSortingType(sortingType: SortingType) = intent {
        reduce { state.copy(selectedSortingType = sortingType) }
    }

    private fun updateYearsRange(years: IntRange) = intent {
        reduce { state.copy(selectedYears = years) }
    }

    private fun toggleAgeRating(ageRating: AgeRating) = intent {
        reduce { state.copy(selectedAgeRatings = state.selectedAgeRatings.toggle(ageRating)) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit) = intent {
        postSideEffect(SearchScreenSideEffect.ShowErrorMessage(error, onRetry))
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item
}
