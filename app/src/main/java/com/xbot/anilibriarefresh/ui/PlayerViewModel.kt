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
import com.xbot.domain.models.TitleDetailModel
import com.xbot.domain.repository.TitleRepository
import com.xbot.media.service.PlayerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    repository: TitleRepository,
    playerProvider: PlayerProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val titleId = savedStateHandle.toRoute<Route.Player>().titleId
    private val titleFlow = repository.getTitle(titleId)
        .catch {
            // TODO: Реализовать обработку ошибок подключения
            Log.e("PlayerViewModel", it.message ?: "")
        }
    val controller: StateFlow<Player?> = playerProvider.player
        .onEach { player ->
            titleFlow.collect { title ->
                with(player) {
                    if (mediaItemCount == 0) addMediaItem(title.toMediaItem())
                    prepare()
                    play()
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )

    private fun TitleDetailModel.toMediaItem(): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
            .setTitle(name)
            // TODO:
            .setArtist(type?.toString())
            .setArtworkUri(Uri.parse("https://anilibria.top${poster.src}"))
            .build()
        return MediaItem.Builder()
            .setMediaMetadata(mediaMetadata)
            .setUri(episodes[3].hls1080)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
    }
}
