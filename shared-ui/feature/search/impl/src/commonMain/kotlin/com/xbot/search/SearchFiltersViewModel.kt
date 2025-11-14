package com.xbot.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Genre
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.usecase.GetCatalogFiltersUseCase
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import com.xbot.search.navigation.SearchFiltersRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchFiltersViewModel(
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val snackbarManager: SnackbarManager,
    private val route: SearchFiltersRoute,
) : ViewModel() {

    private val _availableFilters = MutableStateFlow<CatalogFilters?>(null)
    val availableFilters: StateFlow<CatalogFilters?> = _availableFilters
        .onStart { fetchCatalogFilters() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _availableFilters.value
        )

    private val _selectedFilters = MutableStateFlow(route.initialFilters?.toState() ?: SearchFiltersScreenState())
    val selectedFilters: StateFlow<SearchFiltersScreenState> = _selectedFilters.asStateFlow()

    fun onAction(action: SearchFiltersScreenAction) {
        when (action) {
            is SearchFiltersScreenAction.ToggleGenre -> toggleGenre(action.genre)
            is SearchFiltersScreenAction.ToggleProductionStatus -> toggleProductionStatus(action.productionStatus)
            is SearchFiltersScreenAction.TogglePublishStatus -> togglePublishStatus(action.publishStatus)
            is SearchFiltersScreenAction.ToggleReleaseType -> toggleReleaseType(action.releaseType)
            is SearchFiltersScreenAction.ToggleSeason -> toggleSeason(action.season)
            is SearchFiltersScreenAction.UpdateSortingType -> updateSortingType(action.sortingType)
            is SearchFiltersScreenAction.UpdateYearsRange -> updateYearsRange(action.years)
            is SearchFiltersScreenAction.ToggleAgeRating -> toggleAgeRating(action.ageRating)
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

    private fun updateState(block: (SearchFiltersScreenState) -> SearchFiltersScreenState) {
        _selectedFilters.update { block(_selectedFilters.value) }
    }

    private fun CatalogFilters.toState() = SearchFiltersScreenState(
        selectedGenres = genres.toSet(),
        selectedReleaseTypes = types.toSet(),
        selectedPublishStatuses = publishStatuses.toSet(),
        selectedProductionStatuses = productionStatuses.toSet(),
        selectedSortingType = sortingTypes.first(),
        selectedSeasons = seasons.toSet(),
        selectedAgeRatings = ageRatings.toSet(),
        selectedYears = years,
    )
}

@Stable
data class SearchFiltersScreenState(
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

sealed interface SearchFiltersScreenAction {
    data class ToggleGenre(val genre: Genre) : SearchFiltersScreenAction
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchFiltersScreenAction
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchFiltersScreenAction
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchFiltersScreenAction
    data class ToggleSeason(val season: Season) : SearchFiltersScreenAction
    data class UpdateSortingType(val sortingType: SortingType) : SearchFiltersScreenAction
    data class UpdateYearsRange(val years: IntRange) : SearchFiltersScreenAction
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchFiltersScreenAction
}
