package com.xbot.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xbot.common.LoadableField
import com.xbot.common.asyncLoad
import com.xbot.common.firstError
import com.xbot.common.getOrNull
import com.xbot.common.map
import com.xbot.common.refreshAll
import com.xbot.common.retryErrors
import com.xbot.domain.models.Episode
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
class PlayerViewModel(
    private val releaseId: String,
    private val initialEpisodeOrdinal: Int,
    private val getReleaseUseCase: GetReleaseUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<PlayerScreenState, PlayerScreenSideEffect> {

    private val loadableFields: List<LoadableField<PlayerScreenState>> = listOf(
        LoadableField(selector = { it.episodes }, load = ::loadTitleDetails),
    )

    private val onLoadError: suspend Syntax<PlayerScreenState, PlayerScreenSideEffect>.() -> Unit = {
        loadableFields.firstError(state)?.let { error ->
            postSideEffect(PlayerScreenSideEffect.ShowLoadError(error = error, onRetry = { retry() }))
        }
    }

    override val container: Container<PlayerScreenState, PlayerScreenSideEffect> = container(
        initialState = PlayerScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = PlayerScreenState.serializer(),
    ) {
        refreshAll(loadableFields, onBatchFailure = onLoadError)
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
    }

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
            is PlayerScreenAction.OnQualityChange -> onQualityChange(action.quality)
        }
    }

    private fun retry(): Job = intent {
        retryErrors(loadableFields, onBatchFailure = onLoadError)
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
