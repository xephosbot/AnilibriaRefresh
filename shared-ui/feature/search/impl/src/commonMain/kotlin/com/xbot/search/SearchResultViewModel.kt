package com.xbot.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Release
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.domain.usecase.GetCatalogReleasesPager
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class SearchResultViewModel(
    getCatalogReleasesPager: GetCatalogReleasesPager,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    val searchFieldState: TextFieldState = TextFieldState()
    private val searchQuery: Flow<String> = snapshotFlow { searchFieldState.text.toString() }

    private val _filters: MutableStateFlow<CatalogFilters?> = MutableStateFlow(null)
    val filters: StateFlow<CatalogFilters?> = _filters.asStateFlow()

    @OptIn(FlowPreview::class)
    val searchResult: Flow<PagingData<Release>> = combine(
        searchQuery.debounce(500L), filters
    ) { searchQuery, filters ->
        searchQuery to filters
    }
        .flatMapLatest { (searchQuery, filters) ->
            getCatalogReleasesPager(
                search = searchQuery,
                filters = filters,
            )
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SearchResultScreenAction) {
        when (action) {
            is SearchResultScreenAction.ApplyFilters -> applyFilters(action.filters)
            is SearchResultScreenAction.ShowErrorMessage -> {
                showErrorMessage(action.error.message.orEmpty(), action.onConfirmAction)
            }
        }
    }

    private fun applyFilters(filters: CatalogFilters) {
        _filters.update { filters }
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

sealed interface SearchResultScreenAction {
    data class ApplyFilters(val filters: CatalogFilters) : SearchResultScreenAction
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : SearchResultScreenAction
}
