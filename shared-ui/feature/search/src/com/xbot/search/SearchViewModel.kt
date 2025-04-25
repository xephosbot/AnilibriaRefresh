package com.xbot.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetCatalogFilters
import com.xbot.domain.usecase.GetReleasesPager
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    getReleasesPager: GetReleasesPager,
    private val getCatalogFilters: GetCatalogFilters,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    val searchFieldState: TextFieldState = TextFieldState()
    private val searchQuery: Flow<String> = snapshotFlow {
        searchFieldState.text.toString()
    }

    private val _filters: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState())
    val filters: StateFlow<SearchScreenState> = _filters
        .onStart { fetchCatalogFilters() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _filters.value
        )

    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        searchQuery.debounce(500L),
        filters.debounce(500L)
    ) { searchQuery, filters ->
        searchQuery to filters
    }
        .flatMapLatest { (searchQuery, filters) ->
            getReleasesPager(
                search = searchQuery,
                genres = filters.selectedGenres.toList(),
                types = filters.selectedReleaseTypes.toList(),
                seasons = filters.selectedSeasons.toList(),
                yearsRange = filters.selectedYears,
                sorting = filters.selectedSortingType,
                ageRatings = filters.selectedAgeRatings.toList(),
                publishStatuses = filters.selectedPublishStatuses.toList(),
                productionStatuses = filters.selectedProductionStatuses.toList(),
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
                onSuccess = { catalogFilters ->
                    _filters.update {
                        it.copy(
                            loading = false,
                            genres = catalogFilters.genres,
                            releaseTypes = catalogFilters.releaseTypes,
                            publishStatuses = catalogFilters.publishStatuses,
                            productionStatuses = catalogFilters.productionStatuses,
                            sortingTypes = catalogFilters.sortingTypes,
                            seasons = catalogFilters.seasons,
                            ageRatings = catalogFilters.ageRatings,
                            years = catalogFilters.years,
                            selectedYears = catalogFilters.years
                        )
                    }
                },
                onFailure = {
                    showErrorMessage(it.message.orEmpty(), ::fetchCatalogFilters)
                }
            )
        }
    }

    private fun toggleGenre(genre: Genre) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedGenres = it.selectedGenres.addOrRemove(genre))
            }
        }
    }

    private fun toggleReleaseType(releaseType: ReleaseType) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedReleaseTypes = it.selectedReleaseTypes.addOrRemove(releaseType))
            }
        }
    }

    private fun togglePublishStatus(publishStatus: PublishStatus) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedPublishStatuses = it.selectedPublishStatuses.addOrRemove(publishStatus))
            }
        }
    }

    private fun toggleProductionStatus(productionStatus: ProductionStatus) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedProductionStatuses = it.selectedProductionStatuses.addOrRemove(productionStatus))
            }
        }
    }

    private fun updateSortingType(sortingType: SortingType) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedSortingType = sortingType)
            }
        }
    }

    private fun toggleSeason(season: Season) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedSeasons = it.selectedSeasons.addOrRemove(season))
            }
        }
    }

    private fun updateYearsRange(years: ClosedRange<Int>) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedYears = years)
            }
        }
    }

    private fun toggleAgeRating(ageRating: AgeRating) {
        viewModelScope.launch {
            _filters.update {
                it.copy(selectedAgeRatings = it.selectedAgeRatings.addOrRemove(ageRating))
            }
        }
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
        return if (item in this) {
            this - item
        } else {
            this + item
        }
    }
}

data class SearchScreenState(
    val loading: Boolean = true,
    val genres: List<Genre> = emptyList(),
    val selectedGenres: Set<Genre> = emptySet(),
    val releaseTypes: List<ReleaseType> = emptyList(),
    val selectedReleaseTypes: Set<ReleaseType> = emptySet(),
    val publishStatuses: List<PublishStatus> = emptyList(),
    val selectedPublishStatuses: Set<PublishStatus> = emptySet(),
    val productionStatuses: List<ProductionStatus> = emptyList(),
    val selectedProductionStatuses: Set<ProductionStatus> = emptySet(),
    val sortingTypes: List<SortingType> = emptyList(),
    val selectedSortingType: SortingType = SortingType.FRESH_AT_DESC,
    val seasons: List<Season> = emptyList(),
    val selectedSeasons: Set<Season> = emptySet(),
    val ageRatings: List<AgeRating> = emptyList(),
    val selectedAgeRatings: Set<AgeRating> = emptySet(),
    val years: ClosedRange<Int> = -1..-1,
    val selectedYears: ClosedRange<Int> = years,
)

sealed interface SearchScreenAction {
    data class ToggleGenre(val genre: Genre) : SearchScreenAction
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchScreenAction
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchScreenAction
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchScreenAction
    data class ToggleSeason(val season: Season) : SearchScreenAction
    data class UpdateSortingType(val sortingType: SortingType) : SearchScreenAction
    data class UpdateYearsRange(val years: ClosedRange<Int>) : SearchScreenAction
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchScreenAction
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchScreenAction
}
