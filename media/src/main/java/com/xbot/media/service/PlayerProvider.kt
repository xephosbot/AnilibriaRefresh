package com.xbot.media.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.xbot.media.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerProvider @PublishedApi internal constructor(
    context: Context,
    componentClass: Class<*>,
) {
    private val sessionToken = SessionToken(context, ComponentName(context, componentClass))
    private val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

    val player: Flow<Player> = flow { emit(controllerFuture.await()) }

    companion object {
        inline fun <reified T : BasePlaybackService> create(context: Context): PlayerProvider {
            return PlayerProvider(context, T::class.java)
        }
    }
}
