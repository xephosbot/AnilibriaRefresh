package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingTypes
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
            ageRatings = valuesArray[0] as List<AgeRating>,
            genres = valuesArray[1] as List<GenreModel>,
            productionStatuses = valuesArray[2] as List<ProductionStatus>,
            publishStatuses = valuesArray[3] as List<PublishStatus>,
            seasons = valuesArray[4] as List<Season>,
            sortingTypes = valuesArray[5] as List<SortingTypes>,
            typeReleases = valuesArray[6] as List<ReleaseType>,
            years = valuesArray[7] as List<Int>
        )
    }.catch { error ->
        snackbarManager.showMessage(
            title = StringResource.String(error.message ?: "")
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SearchScreenState.Loading
    )

    fun onAction(action: SearchScreenAction) {
        when(action) {
            is SearchScreenAction.ShowErrorMessage -> {
                //TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = StringResource.String(action.error.message ?: ""),
                    action = MessageAction(
                        title = StringResource.Text(R.string.retry_button),
                        action = action.onConfirmAction
                    )
                )
            }
        }
    }
}

@Stable
sealed interface SearchScreenState {
    data object Loading: SearchScreenState
    data class Success(
        val ageRatings: List<AgeRating>,
        val genres: List<GenreModel>,
        val productionStatuses: List<ProductionStatus>,
        val publishStatuses: List<PublishStatus>,
        val seasons: List<Season>,
        val sortingTypes: List<SortingTypes>,
        val typeReleases: List<ReleaseType>,
        val years: List<Int>
    ): SearchScreenState
}

sealed interface SearchScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit
    ): SearchScreenAction
}