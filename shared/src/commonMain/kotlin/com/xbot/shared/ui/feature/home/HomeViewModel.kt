package com.xbot.shared.ui.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.shared.ui.designsystem.utils.MessageAction
import com.xbot.shared.ui.designsystem.utils.SnackbarManager
import com.xbot.shared.ui.designsystem.utils.StringResource
import com.xbot.shared.domain.models.Release
import com.xbot.shared.domain.models.ReleasesFeed
import com.xbot.shared.domain.usecase.GetReleasesFeed
import com.xbot.shared.domain.usecase.GetReleasesPager
import com.xbot.shared.resources.Res
import com.xbot.shared.resources.button_retry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getReleasesPager: GetReleasesPager,
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getReleasesPager()
        .cachedIn(viewModelScope)

    private val _state: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
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
            getReleasesFeed().fold(
                onSuccess = { releasesFeed ->
                    _state.update { HomeScreenState.Success(releasesFeed) }
                },
                onFailure = {
                    showErrorMessage(it.message.orEmpty(), ::refresh)
                }
            )
        }
    }

    private fun showErrorMessage(error: String, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = StringResource.String(error),
            action = MessageAction(
                title = StringResource.Text(Res.string.button_retry),
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
