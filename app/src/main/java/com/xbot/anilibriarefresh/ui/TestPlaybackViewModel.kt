package com.xbot.anilibriarefresh.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import com.xbot.anilibriarefresh.media.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TestPlaybackViewModel @Inject constructor(
    controller: PlaybackController
) : ViewModel() {

    val player = controller.player
        .onEach { player  ->
            if (!player.isPlaying) {
                player.playHls(TEST_HLS_URI)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private fun Player.playHls(hlsUri: String) {
        val mediaMetadata = MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
            .setTitle(TEST_METADATA_TITLE)
            .setArtist(TEST_METADATA_SUBTITLE)
            .setArtworkUri(Uri.parse(TEST_ARTWORK_URL))
            .build()
        val mediaItem = MediaItem.Builder()
            .setMediaMetadata(mediaMetadata)
            .setUri(hlsUri)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        setMediaItem(mediaItem)
        prepare()
        play()
    }

    companion object {
        const val TEST_METADATA_TITLE = "Истории Ран. Часть 3: Холодная кровь"
        const val TEST_METADATA_SUBTITLE = "Серия 1"
        const val TEST_ARTWORK_URL = "https://anilibria.top/storage/releases/posters/9000/r0mP183P0RBXF6POpeZbiqg8lXnGZkwb.webp"
        const val TEST_HLS_URI = "https://cache-rfn.libria.fun/videos/media/ts/9000/1/1080/7f3c1729ebd24b93d4e0918510004606.m3u8?isWithAds=1&countryIso=RU&isAuthorized=0"
    }
}