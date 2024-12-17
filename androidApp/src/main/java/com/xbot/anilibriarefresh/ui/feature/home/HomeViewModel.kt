package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.models.toTitleUi
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.repository.TitleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    repository: TitleRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val titles: Flow<PagingData<Title>> = repository.getLatestTitles()
        .map { pagingData ->
            pagingData.map(TitleModel::toTitleUi)
        }
        .cachedIn(viewModelScope)

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    val state: StateFlow<HomeScreenState> = refreshTrigger
        .onStart { emit(Unit) }
        .map {
            HomeScreenState.Success(
                recommendedTitles = repository.getRecommendedTitles().map(TitleModel::toTitleUi),
                scheduleTitles = repository.getScheduleTitles().mapValues { (_, titleList) ->
                    titleList.map(TitleModel::toTitleUi)
                },
            )
        }
        .catch { error ->
            // TODO: информативные сообщения для разного типа ошибок
            snackbarManager.showMessage(
                title = StringResource.String(error.message ?: ""),
                action = MessageAction(
                    title = StringResource.Text(R.string.retry_button),
                    action = { onAction(HomeScreenAction.Refresh) },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeScreenState.Loading,
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> {
                // TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = StringResource.String(action.error.message ?: ""),
                    action = MessageAction(
                        title = StringResource.Text(R.string.retry_button),
                        action = action.onConfirmAction,
                    ),
                )
            }
            is HomeScreenAction.Refresh -> {
                viewModelScope.launch {
                    refreshTrigger.emit(Unit)
                }
            }
        }
    }
}

@Stable
sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Success(
        val recommendedTitles: List<Title>,
        val scheduleTitles: Map<DayOfWeek, List<Title>>,
    ) : HomeScreenState
}

sealed interface HomeScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : HomeScreenAction
    data object Refresh : HomeScreenAction
}
