package com.xbot.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xbot.domain.models.Episode
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.player.navigation.PlayerRoute
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: ReleasesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val releaseId = savedStateHandle.toRoute<PlayerRoute>().releaseId
    private val episodeOrdinal = savedStateHandle.toRoute<PlayerRoute>().episodeOrdinal

    private val _state: MutableStateFlow<PlayerScreenState> =
        MutableStateFlow(PlayerScreenState.Loading)
    val state: StateFlow<PlayerScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            repository.getRelease(releaseId.toString()).fold(
                ifRight = { release ->
                    val episode = release.episodes[episodeOrdinal]
                    _state.update { PlayerScreenState.Success(release.episodes) }
                },
                ifLeft = {

                }
            )
        }
    }
}

sealed interface PlayerScreenState {
    object Loading : PlayerScreenState
    data class Success(val episodes: List<Episode>) : PlayerScreenState
}