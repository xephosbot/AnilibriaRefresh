package com.xbot.player.state

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.xbot.domain.models.Episode
import com.xbot.domain.usecase.GetReleaseUseCase
import kotlinx.coroutines.Job
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitExperimental::class)
@KoinViewModel
class PlayerViewModel(
    private val releaseAliasOrId: String,
    private val initialEpisodeOrdinal: Int,
    private val getReleaseUseCase: GetReleaseUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<PlayerScreenState, PlayerScreenSideEffect> {

    override val container: Container<PlayerScreenState, PlayerScreenSideEffect> = container(
        initialState = PlayerScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = PlayerScreenState.serializer(),
    ) {
        startLoadData()
    }

    fun onAction(action: PlayerScreenAction) {
        when (action) {
            is PlayerScreenAction.Refresh -> refresh()
            is PlayerScreenAction.OnEpisodeChange -> onEpisodeChange(action.episode)
            is PlayerScreenAction.OnQualityChange -> onQualityChange(action.quality)
            is PlayerScreenAction.ShowErrorMessage -> showErrorMessage(action.error, action.onRetry)
        }
    }

    private var loadDataJob: Job? = null

    private fun startLoadData() {
        loadDataJob?.cancel()
        loadDataJob = intent {
            reduce { state.copy(isLoading = true) }
            when (val result = getReleaseUseCase(releaseAliasOrId)) {
                is Either.Right -> {
                    val release = result.value
                    val savedOrdinal = savedStateHandle.get<Float>(EPISODE_ORDINAL_KEY)
                    val targetOrdinal = savedOrdinal ?: initialEpisodeOrdinal

                    // Find episode by ordinal, fallback to matching by index if ordinal not found, or first
                    val episode = release.episodes.find { it?.ordinal == targetOrdinal }
                        ?: release.episodes.getOrNull(targetOrdinal.toInt()) // Fallback mostly for safety
                        ?: release.episodes.firstOrNull()

                    reduce {
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
                is Either.Left -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(PlayerScreenSideEffect.ShowErrorMessage(result.value) {
                        onAction(PlayerScreenAction.Refresh)
                    })
                }
            }
        }
    }

    private fun refresh(): Job = intent {
        startLoadData()
    }

    private fun onEpisodeChange(episode: Episode): Job = intent {
        savedStateHandle[EPISODE_ORDINAL_KEY] = episode.ordinal
        reduce {
            val newAvailableQualities = episode.availableQualities
            val newQuality = if (state.quality in newAvailableQualities) {
                state.quality
            } else {
                newAvailableQualities.lastOrNull() ?: VideoQuality.FHD
            }

            state.copy(
                currentEpisode = episode,
                quality = newQuality,
            )
        }
    }

    private fun onQualityChange(quality: VideoQuality): Job = intent {
        reduce { state.copy(quality = quality) }
    }

    private fun showErrorMessage(error: Throwable, onRetry: () -> Unit): Job = intent {
        postSideEffect(PlayerScreenSideEffect.ShowErrorMessage(error, onRetry))
    }

    companion object {
        private const val EPISODE_ORDINAL_KEY = "episode_ordinal"
    }
}

@Serializable
enum class VideoQuality(val title: String) {
    SD("480p"),
    HD("720p"),
    FHD("1080p")
}

@Serializable
@Stable
data class PlayerScreenState(
    @Transient val isLoading: Boolean = true,
    @Transient val episodes: List<Episode?> = emptyList(),
    @Transient val currentEpisode: Episode? = null,
    val quality: VideoQuality = VideoQuality.FHD,
) {
    val availableQualities: List<VideoQuality>
        get() = currentEpisode?.availableQualities ?: emptyList()

    val videoUri: String?
        get() = currentEpisode?.getVideoUri(quality)
}

@Stable
sealed interface PlayerScreenSideEffect {
    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit
    ) : PlayerScreenSideEffect
}

@Stable
sealed interface PlayerScreenAction {
    @Stable
    data object Refresh : PlayerScreenAction

    @Stable
    data class OnEpisodeChange(val episode: Episode) : PlayerScreenAction

    @Stable
    data class OnQualityChange(val quality: VideoQuality) : PlayerScreenAction

    @Stable
    data class ShowErrorMessage(
        val error: Throwable,
        val onRetry: () -> Unit,
    ) : PlayerScreenAction
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
