package com.xbot.media.service

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(UnstableApi::class)
@AndroidEntryPoint
open class BasePlaybackService : MediaSessionService() {
    @Inject
    lateinit var player: Player

    private var mediaSession: MediaSession? = null

    protected open fun getSingleTopActivity(): PendingIntent? = null

    protected open fun getBackStackedActivity(): PendingIntent? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, player).build()
        getSingleTopActivity()?.let { mediaSession?.setSessionActivity(it) }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (player.playWhenReady) {
            // Make sure the service is not in foreground.
            player.pause()
        }
        mediaSession?.release()
        mediaSession = null
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.action == STOP_ACTION) {
            // Stop the foreground service and allow it to be stopped
            stopForeground(STOP_FOREGROUND_REMOVE)
            mediaSession?.player?.stop()
            stopSelf()
        } else {
            // Start the foreground service
            // Perform other necessary operations
        }
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        getBackStackedActivity()?.let { mediaSession?.setSessionActivity(it) }
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    companion object {
        const val STOP_ACTION = "STOP_SERVICE"
    }
}
