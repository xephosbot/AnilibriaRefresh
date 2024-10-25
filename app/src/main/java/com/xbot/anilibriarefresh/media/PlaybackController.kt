package com.xbot.anilibriarefresh.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PlaybackController @Inject constructor(
    context: Context
) {
    private val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
    private val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

    private val _player = MutableStateFlow<Player?>(null)
    val player: Flow<Player?> = _player

    init {
        val mediaItem = MediaItem.Builder()
            .setUri(TEST_HLS_URI)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        controllerFuture.addListener({
            val controller = controllerFuture.get()
            _player.value = controller
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
        }, MoreExecutors.directExecutor())
    }

    companion object {
        const val TEST_HLS_URI = "https://cache-rfn.libria.fun/videos/media/ts/9000/1/1080/7f3c1729ebd24b93d4e0918510004606.m3u8?isWithAds=1&countryIso=RU&isAuthorized=0"
    }
}