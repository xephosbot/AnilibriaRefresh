package com.xbot.anilibriarefresh.ui.feature.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.CatalogFilters
import com.xbot.domain.usecase.GetCatalogFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FiltersViewModel (
    private val getCatalogFilters: GetCatalogFilters,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state: MutableStateFlow<FiltersScreenState> = MutableStateFlow(FiltersScreenState.Loading)
    val state: StateFlow<FiltersScreenState> = _state
        .onStart { fetchCatalogFilters() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun fetchCatalogFilters() {
        viewModelScope.launch {
            getCatalogFilters().fold(
                onSuccess = { catalogFilters ->
                    _state.update { FiltersScreenState.Success(catalogFilters) }
                },
                onFailure = {
                    showErrorMessage(it.localizedMessage.orEmpty(), ::fetchCatalogFilters)
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

sealed interface FiltersScreenState {
    data object Loading : FiltersScreenState
    data class Success(val filters: CatalogFilters): FiltersScreenState
}