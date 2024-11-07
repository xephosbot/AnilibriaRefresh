package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.ui.utils.MessageContent
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.domain.model.AgeRatingEnumModel
import com.xbot.domain.model.GenreModel
import com.xbot.domain.model.ProductionStatusesEnumModel
import com.xbot.domain.model.PublishStatusEnumModel
import com.xbot.domain.model.ReleaseTypeEnumModel
import com.xbot.domain.model.SeasonEnumModel
import com.xbot.domain.model.SortingTypesEnumModel
import com.xbot.domain.repository.FiltersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: FiltersRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    val state: StateFlow<SearchScreenState> = combine(
        repository.getAgeRatings(),
        repository.getGenres(),
        repository.getProductionStatuses(),
        repository.getPublishStatuses(),
        repository.getSeason(),
        repository.getSortingTypes(),
        repository.getTypeReleases(),
        repository.getYears()
    ) { valuesArray ->
        SearchScreenState.Success(
            ageRatings = valuesArray[0] as List<AgeRatingEnumModel>,
            genres = valuesArray[1] as List<GenreModel>,
            productionStatuses = valuesArray[2] as List<ProductionStatusesEnumModel>,
            publishStatuses = valuesArray[3] as List<PublishStatusEnumModel>,
            seasons = valuesArray[4] as List<SeasonEnumModel>,
            sortingTypes = valuesArray[5] as List<SortingTypesEnumModel>,
            typeReleases = valuesArray[6] as List<ReleaseTypeEnumModel>,
            years = valuesArray[7] as List<Int>
        )
    }.catch { error ->
        snackbarManager.showMessage(
            title = MessageContent.String(error.message ?: "")
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SearchScreenState.Loading
    )
}

@Stable
sealed interface SearchScreenState {
    data object Loading: SearchScreenState
    data class Success(
        val ageRatings: List<AgeRatingEnumModel>,
        val genres: List<GenreModel>,
        val productionStatuses: List<ProductionStatusesEnumModel>,
        val publishStatuses: List<PublishStatusEnumModel>,
        val seasons: List<SeasonEnumModel>,
        val sortingTypes: List<SortingTypesEnumModel>,
        val typeReleases: List<ReleaseTypeEnumModel>,
        val years: List<Int>
    ): SearchScreenState
}