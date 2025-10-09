package com.xbot.player

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.designsystem.utils.MessageAction
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.StringResource
import com.xbot.domain.models.Episode
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.player.navigation.PlayerRoute
import com.xbot.resources.Res
import com.xbot.resources.button_retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: ReleasesRepository,
    private val snackbarManager: SnackbarManager,
    private val route: PlayerRoute,
) : ViewModel() {
    private val releaseId = route.releaseId
    private val episodeOrdinal = route.episodeOrdinal

    private val _state: MutableStateFlow<PlayerScreenState> = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            when (val result = repository.getRelease(releaseId.toString())) {
                is Either.Right -> {
                    val release = result.value
                    val episode = release.episodes[episodeOrdinal]
                    _state.update {
                        it.copy(
                            isLoading = false,
                            episodes = release.episodes,
                            currentEpisode = episode
                        )
                    }
                }
                is Either.Left -> showErrorMessage(result.value.toString(), ::fetchTitleDetails)
            }
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
data class PlayerScreenState(
    val isLoading: Boolean = true,
    val episodes: List<Episode> = emptyList(),
    val currentEpisode: Episode? = null,
)