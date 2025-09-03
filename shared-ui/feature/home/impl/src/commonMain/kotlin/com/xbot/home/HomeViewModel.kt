package com.xbot.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetCatalogReleasesPager
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPager,
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getCatalogReleasesPager()
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
                ifRight = { releasesFeed ->
                    _state.update { HomeScreenState.Success(releasesFeed) }
                },
                ifLeft = {
                    showErrorMessage(it.toString().orEmpty(), ::refresh)
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
