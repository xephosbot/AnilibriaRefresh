package com.xbot.anilibriarefresh.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.navigation.toRoute
import com.xbot.anilibriarefresh.navigation.Route
import com.xbot.domain.models.ReleaseDetail
import com.xbot.domain.repository.ReleaseRepository
import com.xbot.media.service.PlayerProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    repository: ReleaseRepository,
    playerProvider: PlayerProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val titleId = savedStateHandle.toRoute<Route.Player>().titleId
    val controller: StateFlow<Player?> = playerProvider.player
        .onEach { player ->
            val title = repository.getRelease(titleId).getOrThrow()
            with(player) {
                clearMediaItems()
                addMediaItem(title.toMediaItem())
                prepare()
                play()
            }
        }
        .catch {
            // TODO: Реализовать обработку ошибок подключения
            Log.e("PlayerViewModel", it.message ?: "")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )

    private fun ReleaseDetail.toMediaItem(): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
            .setTitle(name)
            // TODO:
            .setArtist(type?.toString())
            .setArtworkUri(Uri.parse("https://anilibria.top${poster.src}"))
            .build()
        return MediaItem.Builder()
            .setMediaMetadata(mediaMetadata)
            .setUri(episodes[0].hls1080)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
    }
}
