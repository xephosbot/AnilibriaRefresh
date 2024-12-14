package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.toStringResource
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingType
import com.xbot.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    repository: FiltersRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState.Loading)
    val state: StateFlow<SearchScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _state.update {
                    SearchScreenState.Success(
                        filtersList = listOf(
                            "Жанры" to repository.getGenres()
                                .map { StringResource.String(it.name) },
                            "Возрастной рейтинг" to repository.getAgeRatings()
                                .map(AgeRating::toStringResource),
                            "Статус озвучки" to repository.getProductionStatuses()
                                .map(ProductionStatus::toStringResource),
                            "Выход серий" to repository.getPublishStatuses()
                                .map(PublishStatus::toStringResource),
                            "Сезон" to repository.getSeason().map(Season::toStringResource),
                            "Типы сортировки" to repository.getSortingTypes()
                                .map(SortingType::toStringResource),
                            "Тип релиза" to repository.getTypeReleases()
                                .map(ReleaseType::toStringResource),
                        ),
                        years = repository.getYears(),
                    )
                }
            } catch (error: Exception) {
                snackbarManager.showMessage(
                    title = StringResource.String(error.message ?: ""),
                )
            }
        }
    }

    fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.ShowErrorMessage -> {
                // TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = StringResource.String(action.error.message ?: ""),
                    action = MessageAction(
                        title = StringResource.Text(R.string.retry_button),
                        action = action.onConfirmAction,
                    ),
                )
            }
        }
    }
}

@Stable
sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data class Success(
        val filtersList: List<Pair<String, List<StringResource>>>,
        val years: List<Int>,
    ) : SearchScreenState
}

sealed interface SearchScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchScreenAction
}
