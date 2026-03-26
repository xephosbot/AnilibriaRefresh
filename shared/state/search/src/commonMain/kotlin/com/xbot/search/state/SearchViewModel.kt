@file:UseSerializers(
    GenreSerializer::class,
    IntRangeSerializer::class,
)

package com.xbot.search.state

import androidx.compose.runtime.Stable
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(ExperimentalCoroutinesApi::class, OrbitExperimental::class)
@KoinViewModel(binds = [ViewModel::class])
class SearchViewModel(
    private val getCatalogReleasesPager: GetCatalogReleasesPagerUseCase,
    private val getCatalogFilters: GetCatalogFiltersUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<SearchScreenState, SearchScreenSideEffect> {

    override val container: Container<SearchScreenState, SearchScreenSideEffect> = container(
        initialState = SearchScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = SearchScreenState.serializer(),
    ) {
        loadAvailableFilters()
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

    private var loadFiltersJob: Job? = null

    private fun loadAvailableFilters() {
        loadFiltersJob?.cancel()
        loadFiltersJob = intent {
            getCatalogFilters()
                .catch { error ->
                    postSideEffect(
                        SearchScreenSideEffect.ShowErrorMessage(error) {
                            onAction(SearchScreenAction.Refresh)
                        }
                    )
                }
                .collect { filters ->
                    reduce {
                        state.copy(
                            availableFilters = filters,
                            selectedYears = if (state.selectedYears == IntRange.EMPTY && filters.years != IntRange.EMPTY) {
                                filters.years
                            } else {
                                state.selectedYears
                            },
                        )
                    }
                }
        }
    }

    fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.UpdateQuery -> updateQuery(action.query)
            is SearchScreenAction.ToggleGenre -> intent {
                reduce { state.copy(selectedGenres = state.selectedGenres.toggle(action.genre)) }
            }
            is SearchScreenAction.ToggleProductionStatus -> intent {
                reduce { state.copy(selectedProductionStatuses = state.selectedProductionStatuses.toggle(action.productionStatus)) }
            }
            is SearchScreenAction.TogglePublishStatus -> intent {
                reduce { state.copy(selectedPublishStatuses = state.selectedPublishStatuses.toggle(action.publishStatus)) }
            }
            is SearchScreenAction.ToggleReleaseType -> intent {
                reduce { state.copy(selectedReleaseTypes = state.selectedReleaseTypes.toggle(action.releaseType)) }
            }
            is SearchScreenAction.ToggleSeason -> intent {
                reduce { state.copy(selectedSeasons = state.selectedSeasons.toggle(action.season)) }
            }
            is SearchScreenAction.UpdateSortingType -> intent {
                reduce { state.copy(selectedSortingType = action.sortingType) }
            }
            is SearchScreenAction.UpdateYearsRange -> intent {
                reduce { state.copy(selectedYears = action.years) }
            }
            is SearchScreenAction.ToggleAgeRating -> intent {
                reduce { state.copy(selectedAgeRatings = state.selectedAgeRatings.toggle(action.ageRating)) }
            }
            is SearchScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
            is SearchScreenAction.Refresh -> loadAvailableFilters()
        }
    }

    private fun updateQuery(query: String) = intent {
        reduce { state.copy(query = query) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit) = intent {
        postSideEffect(SearchScreenSideEffect.ShowErrorMessage(error, onRetry))
    }

    private fun <T> Set<T>.toggle(item: T) = if (item in this) this - item else this + item
}

@Serializable
@Stable
data class SearchScreenState(
    val query: String = "",
    @Transient val availableFilters: CatalogFilters = CatalogFilters(),
    val selectedGenres: Set<Genre> = emptySet(),
    val selectedReleaseTypes: Set<ReleaseType> = emptySet(),
    val selectedPublishStatuses: Set<PublishStatus> = emptySet(),
    val selectedProductionStatuses: Set<ProductionStatus> = emptySet(),
    val selectedSortingType: SortingType = SortingType.FRESH_AT_DESC,
    val selectedSeasons: Set<Season> = emptySet(),
    val selectedAgeRatings: Set<AgeRating> = emptySet(),
    val selectedYears: IntRange = IntRange.EMPTY,
) {
    val hasActiveFilters: Boolean
        get() = selectedGenres.isNotEmpty() ||
                selectedReleaseTypes.isNotEmpty() ||
                selectedPublishStatuses.isNotEmpty() ||
                selectedProductionStatuses.isNotEmpty() ||
                selectedSeasons.isNotEmpty() ||
                selectedAgeRatings.isNotEmpty()

    fun toSelectedFilters(): CatalogFilters = CatalogFilters(
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

private fun CatalogFilters.isEmpty(): Boolean =
    genres.isEmpty() &&
    types.isEmpty() &&
    seasons.isEmpty() &&
    years == IntRange.EMPTY &&
    sortingTypes.isEmpty() &&
    ageRatings.isEmpty() &&
    publishStatuses.isEmpty() &&
    productionStatuses.isEmpty()

@Stable
sealed interface SearchScreenAction {
    @Stable
    data class UpdateQuery(val query: String) : SearchScreenAction
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
        val onRetry: () -> Unit,
    ) : SearchScreenAction
    @Stable
    data object Refresh : SearchScreenAction
}

@Stable
sealed interface SearchScreenSideEffect {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : SearchScreenSideEffect
}
