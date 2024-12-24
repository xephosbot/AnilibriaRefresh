package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.repository.ReleaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitleViewModel(
    repository: ReleaseRepository,
    savedStateHandle: SavedStateHandle,
    snackbarManager: SnackbarManager,
) : ViewModel() {
    private val titleId = savedStateHandle.toRoute<Route.Home.Detail>().titleId
    private val _state: MutableStateFlow<TitleScreenState> = MutableStateFlow(TitleScreenState.Loading)
    val state: StateFlow<TitleScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _state.update {
                    TitleScreenState.Success(
                        title = repository.getRelease(titleId).getOrThrow()
                    )
                }
            } catch (error: Exception) {
                // TODO: информативные сообщения для разного типа ошибок
                snackbarManager.showMessage(
                    title = StringResource.String(error.message ?: ""),
                )
            }
        }
    }

    fun onAction(action: TitleScreenAction) {
        // TODO: Actions handling
    }
}

@Stable
sealed interface TitleScreenState {
    data object Loading : TitleScreenState
    data class Success(
        val title: ReleaseDetail,
    ) : TitleScreenState
}

sealed interface TitleScreenAction {
    // TODO: Actions for title screen
}
