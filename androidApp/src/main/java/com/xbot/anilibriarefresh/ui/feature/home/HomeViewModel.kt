package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.utils.MessageAction
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.CatalogFilters
import com.xbot.domain.models.Release
import com.xbot.domain.models.ReleasesFeed
import com.xbot.domain.usecase.GetCatalogFilters
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetReleasesPager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    private val getCatalogFilters: GetCatalogFilters,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    val releases: Flow<PagingData<Release>> = getReleasesPager()
        .cachedIn(viewModelScope)

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Loading)
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

            runCatching {
                coroutineScope {
                    val releasesFeedDeferred = async { getReleasesFeed() }
                    val catalogFiltersDeferred = async { getCatalogFilters() }

                    val releasesFeed = releasesFeedDeferred.await().getOrThrow()
                    val catalogFilters = catalogFiltersDeferred.await().getOrThrow()

                    releasesFeed to catalogFilters
                }
            }.fold(
                onSuccess = { (releasesFeed, catalogFilters) ->
                    _state.update { HomeScreenState.Success(releasesFeed, catalogFilters) }
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
sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Success(
        val releasesFeed: ReleasesFeed,
        val filters: CatalogFilters
    ) : HomeScreenState
}

sealed interface HomeScreenAction {
    data class ShowErrorMessage(
        val error: Throwable,
        val onConfirmAction: () -> Unit,
    ) : HomeScreenAction
    data object Refresh : HomeScreenAction
}
