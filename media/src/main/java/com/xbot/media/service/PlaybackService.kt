package com.xbot.media.service

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject
    lateinit var mediaSession: MediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (player.playWhenReady) {
            // Make sure the service is not in foreground.
            player.pause()
        }
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val player = mediaSession.player
        if (intent?.action == STOP_ACTION) {
            // Stop the foreground service and allow it to be stopped
            stopForeground(STOP_FOREGROUND_REMOVE)
            player.stop()
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
        mediaSession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }

    companion object {
        const val STOP_ACTION = "STOP_SERVICE"
    }
}