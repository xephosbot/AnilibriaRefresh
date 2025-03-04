package com.xbot.player

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.navigation.toRoute
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.repository.ReleaseRepository
import com.xbot.player.navigation.PlayerRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val releaseRepository: ReleaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val releaseId: Int = savedStateHandle.toRoute<PlayerRoute>().id
    private val episodeOrdinal: Int = savedStateHandle.toRoute<PlayerRoute>().episodeOrdinal

    private val _state: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState())
    val state: StateFlow<PlayerUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            releaseRepository.getRelease(releaseId).fold(
                onSuccess = { updatePlaylist(it) },
                onFailure = {}
            )
        }
    }

    private fun updatePlaylist(release: ReleaseDetail) {
        _state.update {
            it.copy(
                playList = release.toMediaItems(),
                currentPlayingItemId = episodeOrdinal
            )
        }
    }

    private fun ReleaseDetail.toMediaItems(): List<MediaItem> {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
            .setTitle(release.name)
            .setArtworkUri(Uri.parse("https://anilibria.top${release.poster.src}"))
            .build()
        return episodes.map { episode ->
            MediaItem.Builder()
                .setMediaMetadata(mediaMetadata)
                .setUri(episode.hls1080 ?: episode.hls720 ?: episode.hls480)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
        }
    }
}

data class PlayerUiState(
    val playList: List<MediaItem> = emptyList(),
    val currentPlayingItemId: Int = 0
)