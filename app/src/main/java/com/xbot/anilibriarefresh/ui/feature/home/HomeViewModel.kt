package com.xbot.anilibriarefresh.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.components.MessageAction
import com.xbot.anilibriarefresh.ui.components.MessageContent
import com.xbot.anilibriarefresh.ui.components.SnackbarManager
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: TitleRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    val titles: Flow<PagingData<TitleModel>> = repository.getLatestTitles()
        .cachedIn(viewModelScope)

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> {
                //TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = MessageContent.String(action.error.message ?: ""),
                    action = MessageAction(
                        textId = R.string.retry_button,
                        action = action.onConfirmAction
                    )
                )
            }
        }
    }
}

sealed interface HomeScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit
    ): HomeScreenAction
}
