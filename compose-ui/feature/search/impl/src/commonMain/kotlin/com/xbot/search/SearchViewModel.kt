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
import com.xbot.localization.UiText
import com.xbot.localization.localizedMessage
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val snackbarManager: SnackbarManager = SnackbarManager,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val searchFieldState: TextFieldState = TextFieldState(savedStateHandle[QUERY_KEY] ?: "")

    private val searchQuery: Flow<String> = snapshotFlow { searchFieldState.text.toString() }
        .onEach { savedStateHandle[QUERY_KEY] = it }

    private val catalogFilters = refreshTrigger
        .flatMapLatest { getCatalogFilters().catch { showErrorMessage(it) { refresh() } } }

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
        searchQuery.debounce(500L), selectedFilters
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
            is SearchScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error, action.onConfirmAction)
            }
            is SearchScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun showErrorMessage(error: Throwable, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = error.localizedMessage(),
            action = MessageAction(
                title = UiText.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item

    private fun updateState(block: (SearchFiltersState) -> SearchFiltersState) {
        _selectedFilters.update { block(selectedFilters.value) }
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

internal fun CatalogFilters.isEmpty(): Boolean =
    genres.isEmpty() &&
    types.isEmpty() &&
    seasons.isEmpty() &&
    years == IntRange.EMPTY &&
    sortingTypes.isEmpty() &&
    ageRatings.isEmpty() &&
    publishStatuses.isEmpty() &&
    productionStatuses.isEmpty()

@Stable
internal sealed interface SearchScreenAction {
    @Stable
    data class ToggleGenre(val genre: Genre) : SearchScreenAction
    @Stable
    data class TogglePublishStatus(val publishStatus: PublishStatus) : SearchScreenAction
    @Stable
    data class ToggleProductionStatus(val productionStatus: ProductionStatus) : SearchScreenAction
    @Stable
    data class ToggleReleaseType(val releaseType: ReleaseType) : SearchScreenAction
    @Stable
    data class ToggleSeason(val season: Season) : SearchScreenAction
    @Stable
    data class UpdateSortingType(val sortingType: SortingType) : SearchScreenAction
    @Stable
    data class UpdateYearsRange(val years: IntRange) : SearchScreenAction
    @Stable
    data class ToggleAgeRating(val ageRating: AgeRating) : SearchScreenAction
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchScreenAction
    @Stable
    data object Refresh : SearchScreenAction
}
