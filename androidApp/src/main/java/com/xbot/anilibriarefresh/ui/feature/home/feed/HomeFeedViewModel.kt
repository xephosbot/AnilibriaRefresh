package com.xbot.anilibriarefresh.ui.feature.home.feed

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetReleasesPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeFeedViewModel(
    getReleasesPager: GetReleasesPager,
    private val getReleasesFeed: GetReleasesFeed,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getReleasesPager()
        .cachedIn(viewModelScope)

    private val _state: MutableStateFlow<HomeFeedScreenState> =
        MutableStateFlow(HomeFeedScreenState.Loading)
    val state: StateFlow<HomeFeedScreenState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    fun onAction(action: HomeFeedScreenAction) {
        when (action) {
            is HomeFeedScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }

            is HomeFeedScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { HomeFeedScreenState.Loading }
            getReleasesFeed().fold(
                onSuccess = { releasesFeed ->
                    _state.update { HomeFeedScreenState.Success(releasesFeed) }
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
                title = StringResource.Text(R.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }
}

@Stable
sealed interface HomeFeedScreenState {
    data object Loading : HomeFeedScreenState
    data class Success(val releasesFeed: ReleasesFeed) : HomeFeedScreenState
}

sealed interface HomeFeedScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : HomeFeedScreenAction

    data object Refresh : HomeFeedScreenAction
}
