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
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.usecase.GetCatalogFiltersUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    private val _query = MutableStateFlow(savedStateHandle[QUERY_KEY] ?: "")
    val query: StateFlow<String> = _query

    private val _sideEffects = Channel<SearchScreenSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val catalogFilters = refreshTrigger
        .flatMapLatest { getCatalogFilters().catch { _sideEffects.trySend(SearchScreenSideEffect.ShowError(it) { refresh() }) } }

    val availableFilters: StateFlow<CatalogFilters> = catalogFilters
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CatalogFilters.create()
        )

    private val _selectedFilters = MutableStateFlow(SearchFiltersState())
    val selectedFilters: StateFlow<SearchFiltersState> = _selectedFilters.asStateFlow()

    init {
        viewModelScope.launch {
            availableFilters
                .filter { it.years != IntRange.EMPTY }
                .take(1)
                .collect { filters ->
                    _selectedFilters.update { it.copy(selectedYears = filters.years) }
                }
        }
    }

    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        _query.debounce(500L), selectedFilters
    ) { query, filters -> query to filters }
        .distinctUntilChanged()
        .flatMapLatest { (query, filters) ->
            getCatalogReleasesPager(
                search = query,
                filters = filters.toCatalogFilters().takeIf { !it.isEmpty() }
            )
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.QueryChanged -> updateQuery(action.query)
            is SearchScreenAction.ToggleGenre -> updateState {
                it.copy(selectedGenres = it.selectedGenres.toggle(action.genre))
            }
            is SearchScreenAction.ToggleProductionStatus -> updateState {
                it.copy(selectedProductionStatuses = it.selectedProductionStatuses.toggle(action.productionStatus))
            }
            is SearchScreenAction.TogglePublishStatus -> updateState {
                it.copy(selectedPublishStatuses = it.selectedPublishStatuses.toggle(action.publishStatus))
            }
            is SearchScreenAction.ToggleReleaseType -> updateState {
                it.copy(selectedReleaseTypes = it.selectedReleaseTypes.toggle(action.releaseType))
            }
            is SearchScreenAction.ToggleSeason -> updateState {
                it.copy(selectedSeasons = it.selectedSeasons.toggle(action.season))
            }
            is SearchScreenAction.UpdateSortingType -> updateState {
                it.copy(selectedSortingType = action.sortingType)
            }
            is SearchScreenAction.UpdateYearsRange -> updateState {
                it.copy(selectedYears = action.years)
            }
            is SearchScreenAction.ToggleAgeRating -> updateState {
                it.copy(selectedAgeRatings = it.selectedAgeRatings.toggle(action.ageRating))
            }
            is SearchScreenAction.Refresh -> refresh()
        }
    }

    private fun updateQuery(query: String) {
        savedStateHandle[QUERY_KEY] = query
        _query.value = query
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item

    private fun updateState(block: (SearchFiltersState) -> SearchFiltersState) {
        _selectedFilters.update { block(selectedFilters.value) }
    }

    companion object {
        private const val QUERY_KEY = "query"
    }
}

data class SearchFiltersState(
    val loading: Boolean = true,
    val selectedGenres: Set<Genre> = emptySet(),
    val selectedReleaseTypes: Set<ReleaseType> = emptySet(),
    val selectedPublishStatuses: Set<PublishStatus> = emptySet(),
    val selectedProductionStatuses: Set<ProductionStatus> = emptySet(),
    val selectedSortingType: SortingType = SortingType.FRESH_AT_DESC,
    val selectedSeasons: Set<Season> = emptySet(),
    val selectedAgeRatings: Set<AgeRating> = emptySet(),
    val selectedYears: IntRange = IntRange.EMPTY,
) {
    fun toCatalogFilters() = CatalogFilters(
        genres = selectedGenres.toList(),
        types = selectedReleaseTypes.toList(),
        publishStatuses = selectedPublishStatuses.toList(),
        productionStatuses = selectedProductionStatuses.toList(),
        sortingTypes = listOf(selectedSortingType),
        seasons = selectedSeasons.toList(),
        ageRatings = selectedAgeRatings.toList(),
        years = selectedYears,
    )
}

fun CatalogFilters.isEmpty(): Boolean =
    genres.isEmpty() &&
    types.isEmpty() &&
    seasons.isEmpty() &&
    years == IntRange.EMPTY &&
    sortingTypes.isEmpty() &&
    ageRatings.isEmpty() &&
    publishStatuses.isEmpty() &&
    productionStatuses.isEmpty()

sealed interface SearchScreenAction {
    data class QueryChanged(val query: String) : SearchScreenAction
    data class ToggleGenre(val genre: Genre) : SearchScreenAction
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchScreenAction
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchScreenAction
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchScreenAction
    data class ToggleSeason(val season: Season) : SearchScreenAction
    data class UpdateSortingType(val sortingType: SortingType) : SearchScreenAction
    data class UpdateYearsRange(val years: IntRange) : SearchScreenAction
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchScreenAction
    data object Refresh : SearchScreenAction
}

sealed interface SearchScreenSideEffect {
    data class ShowError(val error: Throwable, val retryAction: () -> Unit) : SearchScreenSideEffect
}
