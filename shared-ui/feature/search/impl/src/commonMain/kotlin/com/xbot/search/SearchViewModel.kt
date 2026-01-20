package com.xbot.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
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
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val snackbarManager: SnackbarManager,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchFieldState: TextFieldState = TextFieldState(savedStateHandle[QUERY_KEY] ?: "")
    private val searchQuery: Flow<String> = snapshotFlow { searchFieldState.text.toString() }
        .onEach { savedStateHandle[QUERY_KEY] = it }

    private val _availableFilters = MutableStateFlow<CatalogFilters?>(null)
    val availableFilters: StateFlow<CatalogFilters?> = _availableFilters
        .onStart { fetchCatalogFilters() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _availableFilters.value
        )

    private val _selectedFilters = MutableStateFlow(SearchFiltersState())
    val selectedFilters: StateFlow<SearchFiltersState> = _selectedFilters.asStateFlow()

    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        searchQuery.debounce(500L), selectedFilters
    ) { searchQuery, filters ->
        searchQuery to filters
    }
        .flatMapLatest { (searchQuery, filters) ->
            val catalogFilters = filters.toCatalogFilters()
            getCatalogReleasesPager(
                search = searchQuery,
                filters = if (catalogFilters.isEmpty()) null else catalogFilters,
            )
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.ToggleGenre -> toggleGenre(action.genre)
            is SearchScreenAction.ToggleProductionStatus -> toggleProductionStatus(action.productionStatus)
            is SearchScreenAction.TogglePublishStatus -> togglePublishStatus(action.publishStatus)
            is SearchScreenAction.ToggleReleaseType -> toggleReleaseType(action.releaseType)
            is SearchScreenAction.ToggleSeason -> toggleSeason(action.season)
            is SearchScreenAction.UpdateSortingType -> updateSortingType(action.sortingType)
            is SearchScreenAction.UpdateYearsRange -> updateYearsRange(action.years)
            is SearchScreenAction.ToggleAgeRating -> toggleAgeRating(action.ageRating)
            is SearchScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }
        }
    }

    private fun fetchCatalogFilters() {
        viewModelScope.launch {
            getCatalogFilters().fold(
                ifRight = { catalogFilters ->
                    _availableFilters.update { catalogFilters }
                    if (_selectedFilters.value.selectedYears == IntRange.EMPTY) {
                        updateState { it.copy(selectedYears = catalogFilters.years) }
                    }
                },
                ifLeft = {
                    showErrorMessage(it.toString(), ::fetchCatalogFilters)
                }
            )
        }
    }

    private fun toggleGenre(genre: Genre) = updateState {
        it.copy(selectedGenres = it.selectedGenres.addOrRemove(genre))
    }

    private fun toggleReleaseType(releaseType: ReleaseType) = updateState {
        it.copy(selectedReleaseTypes = it.selectedReleaseTypes.addOrRemove(releaseType))
    }

    private fun togglePublishStatus(publishStatus: PublishStatus) = updateState {
        it.copy(selectedPublishStatuses = it.selectedPublishStatuses.addOrRemove(publishStatus))
    }

    private fun toggleProductionStatus(productionStatus: ProductionStatus) = updateState {
        it.copy(selectedProductionStatuses = it.selectedProductionStatuses.addOrRemove(productionStatus))
    }

    private fun updateSortingType(sortingType: SortingType) = updateState {
        it.copy(selectedSortingType = sortingType)
    }

    private fun toggleSeason(season: Season) = updateState {
        it.copy(selectedSeasons = it.selectedSeasons.addOrRemove(season))
    }

    private fun updateYearsRange(years: IntRange) = updateState {
        it.copy(selectedYears = years)
    }

    private fun toggleAgeRating(ageRating: AgeRating) = updateState {
        it.copy(selectedAgeRatings = it.selectedAgeRatings.addOrRemove(ageRating))
    }

    private fun showErrorMessage(error: String, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
            action = MessageAction(
                title = StringResource.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }

    private fun <T> Set<T>.addOrRemove(item: T): Set<T> {
        return if (item in this) this - item else this + item
    }

    private fun updateState(block: (SearchFiltersState) -> SearchFiltersState) {
        _selectedFilters.update { block(_selectedFilters.value) }
    }

    companion object {
        private const val QUERY_KEY = "query"
    }
}

@Stable
internal data class SearchFiltersState(
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

private fun CatalogFilters.isEmpty(): Boolean {
    return this.genres.isEmpty() && this.types.isEmpty() && this.seasons.isEmpty()
            && this.years == IntRange.EMPTY && this.sortingTypes.isEmpty()
            && this.ageRatings.isEmpty() && this.publishStatuses.isEmpty() && this.productionStatuses.isEmpty()
}

internal sealed interface SearchScreenAction {
    data class ToggleGenre(val genre: Genre) : SearchScreenAction
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchScreenAction
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchScreenAction
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchScreenAction
    data class ToggleSeason(val season: Season) : SearchScreenAction
    data class UpdateSortingType(val sortingType: SortingType) : SearchScreenAction
    data class UpdateYearsRange(val years: IntRange) : SearchScreenAction
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchScreenAction
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchScreenAction
}
