package com.xbot.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xbot.common.AsyncResult
import com.xbot.common.asyncLoad
import com.xbot.common.getOrNull
import com.xbot.common.map
import com.xbot.domain.models.Episode
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class PlayerViewModel(
    private val releaseId: String,
    private val initialEpisodeOrdinal: Int,
    private val getReleaseUseCase: GetReleaseUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<PlayerScreenState, PlayerScreenSideEffect> {

    override val container: Container<PlayerScreenState, PlayerScreenSideEffect> = container(
        initialState = PlayerScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = PlayerScreenState.serializer(),
    ) {
        loadTitleDetails()
    }

    private fun loadTitleDetails(): Job = intent {
        asyncLoad(
            request = { getReleaseUseCase(releaseId) },
            reducer = { details ->
                val episode = details.map { release ->
                    release.episodes.find { it.ordinal == initialEpisodeOrdinal.toFloat() }
                        ?: release.episodes.getOrNull(initialEpisodeOrdinal)
                        ?: release.episodes.firstOrNull()
                }.getOrNull()

                val newAvailableQualities = episode?.availableQualities ?: emptyList()
                val newQuality = if (state.quality in newAvailableQualities) {
                    state.quality
                } else {
                    newAvailableQualities.lastOrNull() ?: VideoQuality.FHD
                }

                copy(
                    episodes = details.map { it.episodes },
                    currentEpisode = episode,
                    quality = newQuality
                )
            }
        )

        val error = (state.episodes as? AsyncResult.Error)?.error
        if (error is Throwable) {
            postSideEffect(PlayerScreenSideEffect.ShowLoadError(error = error, onRetry = { retry() }))
        }
    }

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
            is PlayerScreenAction.OnQualityChange -> onQualityChange(action.quality)
        }
    }

    private fun retry() {
        loadTitleDetails()
    }

    private fun onEpisodeChange(episode: Episode) = intent {
        val newAvailableQualities = episode.availableQualities
        val newQuality = if (state.quality in newAvailableQualities) {
            state.quality
        } else {
            newAvailableQualities.lastOrNull() ?: VideoQuality.FHD
        }

        reduce {
            state.copy(
                currentEpisode = episode,
                quality = newQuality,
            )
        }
    }

    private fun onQualityChange(quality: VideoQuality) = intent {
        reduce {
            state.copy(quality = quality)
        }
    }
}
