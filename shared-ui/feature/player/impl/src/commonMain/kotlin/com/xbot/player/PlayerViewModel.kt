package com.xbot.player

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
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

internal class PlayerViewModel(
    private val repository: ReleasesRepository,
    private val snackbarManager: SnackbarManager,
    private val savedStateHandle: SavedStateHandle,
    private val route: PlayerRoute,
) : ViewModel() {
    private val releaseId = route.releaseId
    private val initialEpisodeOrdinal = route.episodeOrdinal

    private val _state: MutableStateFlow<PlayerScreenState> = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
        }
    }

    private fun onEpisodeChange(episode: Episode) {
        savedStateHandle[EPISODE_ORDINAL_KEY] = episode.ordinal
        _state.update { it.copy(currentEpisode = episode) }
    }

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            when (val result = repository.getRelease(releaseId.toString())) {
                is Either.Right -> {
                    val release = result.value
                    val savedOrdinal = savedStateHandle.get<Float>(EPISODE_ORDINAL_KEY)
                    val targetOrdinal = savedOrdinal ?: initialEpisodeOrdinal
                    
                    // Find episode by ordinal, fallback to matching by index if ordinal not found, or first
                    val episode = release.episodes.find { it.ordinal == targetOrdinal } 
                        ?: release.episodes.getOrNull(targetOrdinal.toInt()) // Fallback mostly for safety
                        ?: release.episodes.firstOrNull()
                        
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

    companion object {
        private const val EPISODE_ORDINAL_KEY = "episode_ordinal"
    }
}

@Stable
internal data class PlayerScreenState(
    val isLoading: Boolean = true,
    val episodes: List<Episode> = emptyList(),
    val currentEpisode: Episode? = null,
)

internal sealed interface PlayerScreenAction {
    data class OnEpisodeChange(val episode: Episode) : PlayerScreenAction
}
