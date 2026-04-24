package com.xbot.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xbot.common.asyncLoad
import com.xbot.common.getOrNull
import com.xbot.common.map
import com.xbot.domain.models.Episode
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@KoinViewModel
class PlayerViewModel(
    private val releaseId: String,
    private val initialEpisodeOrdinal: Int,
    private val getReleaseUseCase: GetReleaseUseCase,
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel(), ContainerHost<PlayerScreenState, PlayerScreenSideEffect> {

    override val container: Container<PlayerScreenState, PlayerScreenSideEffect> = container(
        initialState = PlayerScreenState(),
        savedStateHandle = savedStateHandle ?: SavedStateHandle(),
        serializer = PlayerScreenState.serializer(),
    ) {
        loadTitleDetails()
    }

    private fun loadTitleDetails(): Job = intent {
        asyncLoad(
            request = { getReleaseUseCase(releaseId) },
            onError = { error -> showErrorMessage(error) { loadTitleDetails() } },
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
    }

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
            is PlayerScreenAction.OnQualityChange -> onQualityChange(action.quality)
        }
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

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(PlayerScreenSideEffect.ShowErrorMessage(error, onRetry))
    }
}
