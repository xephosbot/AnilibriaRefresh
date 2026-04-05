package com.xbot.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.xbot.domain.models.Episode
import com.xbot.domain.repository.ReleasesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PlayerViewModel(
    private val repository: ReleasesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val releaseId: Int,
    private val initialEpisodeOrdinal: Int,
) : ViewModel() {

    private val _sideEffects = Channel<PlayerScreenSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _state: MutableStateFlow<PlayerScreenState> = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state
        .onStart { fetchTitleDetails() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = _state.value
        )

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
            is PlayerScreenAction.OnQualityChange -> onQualityChange(action.quality)
        }
    }

    private fun onEpisodeChange(episode: Episode) {
        savedStateHandle[EPISODE_ORDINAL_KEY] = episode.ordinal
        _state.update {
            val newAvailableQualities = episode.availableQualities
            val newQuality = if (it.quality in newAvailableQualities) {
                it.quality
            } else {
                newAvailableQualities.lastOrNull() ?: VideoQuality.FHD
            }

            it.copy(
                currentEpisode = episode,
                quality = newQuality,
            )
        }
    }

    private fun onQualityChange(quality: VideoQuality) {
        _state.update { it.copy(quality = quality) }
    }

    private fun fetchTitleDetails() {
        viewModelScope.launch {
            when (val result = repository.getRelease(releaseId.toString())) {
                is Either.Right -> {
                    val release = result.value
                    val savedOrdinal = savedStateHandle.get<Float>(EPISODE_ORDINAL_KEY)
                    val targetOrdinal = savedOrdinal ?: initialEpisodeOrdinal.toFloat()

                    val episode = release.episodes.find { it?.ordinal == targetOrdinal }
                        ?: release.episodes.getOrNull(initialEpisodeOrdinal)
                        ?: release.episodes.firstOrNull()

                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            episodes = release.episodes
                        ).let {
                            if (episode != null) {
                                val newAvailableQualities = episode.availableQualities
                                val newQuality = if (it.quality in newAvailableQualities) {
                                    it.quality
                                } else {
                                    newAvailableQualities.lastOrNull() ?: VideoQuality.FHD
                                }

                                it.copy(
                                    currentEpisode = episode,
                                    quality = newQuality,
                                )
                            } else it
                        }
                    }
                }
                is Either.Left -> _sideEffects.trySend(
                    PlayerScreenSideEffect.ShowError(result.value, ::fetchTitleDetails)
                )
            }
        }
    }

    companion object {
        private const val EPISODE_ORDINAL_KEY = "episode_ordinal"
    }
}

enum class VideoQuality(val title: String) {
    SD("480p"),
    HD("720p"),
    FHD("1080p")
}

data class PlayerScreenState(
    val isLoading: Boolean = true,
    val episodes: List<Episode?> = emptyList(),
    val currentEpisode: Episode? = null,
    val quality: VideoQuality = VideoQuality.FHD,
) {
    val availableQualities: List<VideoQuality>
        get() = currentEpisode?.availableQualities ?: emptyList()

    val videoUri: String?
        get() = currentEpisode?.getVideoUri(quality)
}

sealed interface PlayerScreenAction {
    data class OnEpisodeChange(val episode: Episode) : PlayerScreenAction
    data class OnQualityChange(val quality: VideoQuality) : PlayerScreenAction
}

sealed interface PlayerScreenSideEffect {
    data class ShowError(val error: Throwable, val retryAction: () -> Unit) : PlayerScreenSideEffect
}

private val Episode.availableQualities: List<VideoQuality>
    get() = buildList {
        if (!hls480.isNullOrEmpty()) add(VideoQuality.SD)
        if (!hls720.isNullOrEmpty()) add(VideoQuality.HD)
        if (!hls1080.isNullOrEmpty()) add(VideoQuality.FHD)
    }

private fun Episode.getVideoUri(quality: VideoQuality): String? {
    return when (quality) {
        VideoQuality.SD -> hls480
        VideoQuality.HD -> hls720
        VideoQuality.FHD -> hls1080
    }
}
