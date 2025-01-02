package com.xbot.anilibriarefresh.ui.feature.home.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.usecase.GetReleasesPager
import kotlinx.coroutines.flow.Flow

class SearchResultViewModel(
    getReleasesPager: GetReleasesPager,
    savedStateHandle: SavedStateHandle,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val searchQuery = savedStateHandle.toRoute<Route.Home.SearchResult>().searchQuery
    val releases: Flow<PagingData<Release>> = getReleasesPager(searchQuery)
        .cachedIn(viewModelScope)

    fun onAction(action: SearchResultScreenAction) {
        when (action) {
            is SearchResultScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }
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

sealed interface SearchResultScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchResultScreenAction
}
