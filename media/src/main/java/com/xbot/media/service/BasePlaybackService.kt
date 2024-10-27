package com.xbot.media.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BasePlaybackService : MediaSessionService() {
    @Inject
    lateinit var mediaSession: MediaSession

    protected open fun getSingleTopActivity(): PendingIntent? = null

    protected open fun getBackStackedActivity(): PendingIntent? = null

    override fun onCreate() {
        super.onCreate()
        getSingleTopActivity()?.let { mediaSession.setSessionActivity(it) }
    }

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
        getBackStackedActivity()?.let { mediaSession.setSessionActivity(it) }
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
    }

    companion object {
        const val STOP_ACTION = "STOP_SERVICE"
    }
}