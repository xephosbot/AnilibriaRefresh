package com.xbot.anilibriarefresh.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaybackController @Inject constructor(
    context: Context
) {
    private val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
    private val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

    val player: Flow<Player> = flow { emit(controllerFuture.await()) }
}