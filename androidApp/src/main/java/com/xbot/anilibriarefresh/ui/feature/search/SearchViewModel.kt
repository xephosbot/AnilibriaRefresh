package com.xbot.anilibriarefresh.ui.feature.search

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    repository: CatalogRepository,
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
                            /*"Жанры" to repository.getGenres().getOrThrow()
                                .map { StringResource.String(it.name) },
                            "Возрастной рейтинг" to repository.getAgeRatings().getOrThrow()
                                .map(AgeRating::toStringResource),
                            "Статус озвучки" to repository.getProductionStatuses().getOrThrow()
                                .map(ProductionStatus::toStringResource),
                            "Выход серий" to repository.getPublishStatuses().getOrThrow()
                                .map(PublishStatus::toStringResource),
                            "Сезон" to repository.getSeasons().map(Season::toStringResource),
                            "Типы сортировки" to repository.getSortingTypes().getOrThrow()
                                .map(SortingType::toStringResource),
                            "Тип релиза" to repository.getReleaseType().getOrThrow()
                                .map(ReleaseType::toStringResource),*/
                        ),
                        years = repository.getYears().getOrThrow(),
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
