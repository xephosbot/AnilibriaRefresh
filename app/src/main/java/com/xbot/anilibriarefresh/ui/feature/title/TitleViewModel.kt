package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xbot.anilibriarefresh.models.TitleDetail
import com.xbot.anilibriarefresh.models.toTitleDetailUi
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.StringResource
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(
    repository: TitleRepository,
    savedStateHandle: SavedStateHandle,
    snackbarManager: SnackbarManager
) : ViewModel() {
    private val titleId = savedStateHandle.toRoute<Route.Home.Detail>().titleId
    val state: StateFlow<TitleScreenState> = repository.getTitle(titleId)
        .catch { error ->
            //TODO: информативные сообщения для разного типа ошибок
            snackbarManager.showMessage(
                title = StringResource.String(error.message ?: "")
            )
        }.map { title ->
            TitleScreenState.Success(title.toTitleDetailUi())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TitleScreenState.Loading
        )

    fun onAction(action: TitleScreenAction) {
        //TODO: Actions handling
    }
}

@Stable
sealed interface TitleScreenState {
    data object Loading: TitleScreenState
    data class Success (
        val title: TitleDetail
    ): TitleScreenState
}

sealed interface TitleScreenAction {
    //TODO: Actions for title screen
}