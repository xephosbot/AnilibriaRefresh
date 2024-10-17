package com.xbot.anilibriarefresh.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.MessageContent
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: TitleRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    val titles: Flow<PagingData<TitleModel>> = repository.getLatestTitles()
        .cachedIn(viewModelScope)

    val state: StateFlow<HomeScreenState> = combine(
        repository.getRecommendedTitles(),
        repository.getFavoriteTitles()
    ) { recommendedTitles, favoriteTitles ->
        HomeScreenState.Success(
            recommendedTitles = recommendedTitles,
            favoriteTitles = favoriteTitles
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeScreenState.Loading
    )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> {
                //TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = MessageContent.String(action.error.message ?: ""),
                    action = MessageAction(
                        title = MessageContent.Text(R.string.retry_button),
                        action = action.onConfirmAction
                    )
                )
            }
        }
    }
}

sealed interface HomeScreenState {
    data object Loading: HomeScreenState
    data class Success(
        val recommendedTitles: List<TitleModel>,
        val favoriteTitles: List<TitleModel>
    ): HomeScreenState
}

sealed interface HomeScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit
    ): HomeScreenAction
}
