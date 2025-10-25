package com.xbot.title

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Episode
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import com.xbot.title.navigation.TitleEpisodesRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TitleEpisodesViewModel(
    private val repository: ReleasesRepository,
    private val snackbarManager: SnackbarManager,
    private val route: TitleEpisodesRoute,
) : ViewModel() {
    private val aliasOrId = route.aliasOrId

    private val _state: MutableStateFlow<TitleEpisodesScreenState> =
        MutableStateFlow(TitleEpisodesScreenState.Loading)
    val state: StateFlow<TitleEpisodesScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            repository.getRelease(aliasOrId).fold(
                ifRight = { title ->
                    _state.update { TitleEpisodesScreenState.Success(title.release.id, title.episodes) }
                },
                ifLeft = {
                    showErrorMessage(it.toString(), ::fetchTitleDetails)
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
sealed interface TitleEpisodesScreenState {
    data object Loading : TitleEpisodesScreenState
    data class Success(
        val releaseId: Int,
        val episodes: List<Episode>,
    ) : TitleEpisodesScreenState
}
