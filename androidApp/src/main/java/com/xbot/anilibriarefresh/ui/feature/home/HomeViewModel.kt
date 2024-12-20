package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.models.Title
import com.xbot.anilibriarefresh.models.toTitleUi
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.data.datasource.CommonPagingSource
import com.xbot.data.datasource.CommonPagingSource.Companion.NETWORK_PAGE_SIZE
import com.xbot.domain.models.TitleModel
import com.xbot.domain.models.enums.DayOfWeek
import com.xbot.domain.repository.TitleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TitleRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val pager = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            prefetchDistance = NETWORK_PAGE_SIZE,
            enablePlaceholders = true,
            jumpThreshold = NETWORK_PAGE_SIZE * 3,
        ),
        pagingSourceFactory = {
            CommonPagingSource { page, limit -> repository.getCatalogTitles(page, limit) }
        },
    )
    val titles: Flow<PagingData<Title>> = pager.flow.map { pagingData ->
        pagingData.map(TitleModel::toTitleUi)
    }.cachedIn(viewModelScope)

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state
        .onStart { refresh() }
        .catch { error ->
            // TODO: информативные сообщения для разного типа ошибок
            showErrorMessage(error.message.orEmpty(), ::refresh)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeScreenState.Loading,
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> {
                // TODO: информативные сообщения для разного типа ошибок
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }
            is HomeScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val recommendedTitles = async { repository.getRecommendedTitles() }
            val scheduleWeek = async { repository.getScheduleWeek() }
            val successState = HomeScreenState.Success(
                recommendedTitles = recommendedTitles.await().map(TitleModel::toTitleUi),
                scheduleTitles = scheduleWeek.await().mapValues { (_, titleList) ->
                    titleList.map(TitleModel::toTitleUi)
                },
            )
            _state.update { successState }
        }
    }

    private fun showErrorMessage(error: String, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
            action = MessageAction(
                title = StringResource.Text(R.string.retry_button),
                action = onConfirmAction,
            ),
        )
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
