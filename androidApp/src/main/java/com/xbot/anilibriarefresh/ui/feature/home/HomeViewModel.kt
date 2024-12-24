package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.models.Release
import com.xbot.domain.repository.ReleaseRepository
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetReleasesPager
import com.xbot.domain.usecase.GetReleasesPager.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getReleasesPager: GetReleasesPager,
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val titles: Flow<PagingData<Release>> = getReleasesPager.invoke()
        .cachedIn(viewModelScope)

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }
            is HomeScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { HomeScreenState.Loading }
        }

        viewModelScope.launch {
            getReleasesFeed.invoke().fold(
                onSuccess = { releasesFeed ->
                    _state.update { HomeScreenState.Success(releasesFeed) }
                },
                onFailure = {
                    showErrorMessage(it.localizedMessage.orEmpty(), ::refresh)
                }
            )
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
    data class Success(val releasesFeed: ReleasesFeed) : HomeScreenState
}

sealed interface HomeScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : HomeScreenAction
    data object Refresh : HomeScreenAction
}
