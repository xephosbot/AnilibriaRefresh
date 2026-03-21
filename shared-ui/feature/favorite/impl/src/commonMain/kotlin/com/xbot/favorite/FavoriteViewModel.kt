package com.xbot.favorite

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetFavoriteReleasesPagerUseCase
import com.xbot.localization.UiText
import com.xbot.localization.localizedMessage
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoriteViewModel(
    private val getFavoriteReleasesPager: GetFavoriteReleasesPagerUseCase,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val favoriteReleases: Flow<PagingData<Release>> = refreshTrigger.flatMapLatest {
        getFavoriteReleasesPager()
    }.cachedIn(viewModelScope)

    private val _state = MutableStateFlow(FavoriteScreenState())
    val state: StateFlow<FavoriteScreenState> = _state.asStateFlow()

    fun onAction(action: FavoriteScreenAction) {
        when (action) {
            is FavoriteScreenAction.Refresh -> refresh()
            is FavoriteScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onConfirmAction)
        }
    }

    private fun refresh() {
        refreshTrigger.update { it + 1 }
    }

    private fun showErrorMessage(error: Throwable, onConfirmAction: () -> Unit) {
        snackbarManager.showMessage(
            title = error.localizedMessage(),
            action = MessageAction(
                title = UiText.Text(Res.string.button_retry),
                action = onConfirmAction,
            ),
        )
    }
}

@Stable
internal data class FavoriteScreenState(
    val isLoading: Boolean = false,
)

@Stable
internal sealed interface FavoriteScreenAction {
    @Stable
    data object Refresh : FavoriteScreenAction

    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : FavoriteScreenAction
}
