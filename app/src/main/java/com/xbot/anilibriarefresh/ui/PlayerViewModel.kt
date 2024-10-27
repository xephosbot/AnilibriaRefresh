package com.xbot.anilibriarefresh.ui

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import com.xbot.media.service.PlayerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    playerProvider: PlayerProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val controller: StateFlow<Player?> = playerProvider.player
        .onEach { player ->
            player.playHls()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private fun Player.playHls() {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
            .setTitle(TEST_METADATA_TITLE)
            .setArtist(TEST_METADATA_SUBTITLE)
            .setArtworkUri(Uri.parse(TEST_ARTWORK_URL))
            .build()
        val mediaItem = MediaItem.Builder()
            .setMediaMetadata(mediaMetadata)
            .setUri(TEST_HLS_URI)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        if (mediaItemCount == 0) addMediaItem(mediaItem)
        prepare()
        play()
    }

    companion object {
        private const val TEST_METADATA_TITLE = "Дандадан"
        private const val TEST_METADATA_SUBTITLE = "Серия 1"
        private const val TEST_ARTWORK_URL = "https://anilibria.top/storage/releases/posters/9789/X23LsN6aamdmQlFsKA6Er2sYnwcmoMzQ.webp"
        private const val TEST_HLS_URI = "https://cache-rfn.libria.fun/videos/media/ts/9789/1/1080/572da4181b9e639b2728b5e34ec484b9.m3u8?isWithAds=1&countryIso=RU&isAuthorized=0"
    }
}