package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.toStringResource
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
        repository.getGenres(),
        repository.getAgeRatings(),
        repository.getProductionStatuses(),
        repository.getPublishStatuses(),
        repository.getSeason(),
        repository.getSortingTypes(),
        repository.getTypeReleases(),
        repository.getYears()
    ) { valuesArray ->
        SearchScreenState.Success(
            filtersList = listOf(
                "Жанры" to valuesArray[0].typedMap<GenreModel> { StringResource.String(it.name) },
                "Возрастной рейтинг" to valuesArray[1].typedMap(AgeRating::toStringResource),
                "Статус озвучки" to valuesArray[2].typedMap(ProductionStatus::toStringResource),
                "Выход серий" to valuesArray[3].typedMap(PublishStatus::toStringResource),
                "Сезон" to valuesArray[4].typedMap(Season::toStringResource),
                "Типы сортировки" to valuesArray[5].typedMap(SortingTypes::toStringResource),
                "Тип релиза" to valuesArray[6].typedMap(ReleaseType::toStringResource)
            ),
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

    @Suppress("UNCHECKED_CAST")
    private fun <T> List<Any>.typedMap(transform: (T) -> StringResource): List<StringResource> {
        return (this as List<T>).map(transform)
    }
}

@Stable
sealed interface SearchScreenState {
    data object Loading: SearchScreenState
    data class Success(
        val filtersList: List<Pair<String, List<StringResource>>>,
        val years: List<Int>
    ): SearchScreenState
}

sealed interface SearchScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit
    ): SearchScreenAction
}