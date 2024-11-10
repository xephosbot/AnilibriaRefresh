package com.xbot.anilibriarefresh.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.xbot.anilibriarefresh.ui.MainActivity
import com.xbot.anilibriarefresh.ui.PlayerActivity
import com.xbot.media.service.BasePlaybackService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaybackService : BasePlaybackService() {
    override fun getSingleTopActivity(): PendingIntent? {
        val intent = Intent(this, PlayerActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun getBackStackedActivity(): PendingIntent? {
        return TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))
            addNextIntent(Intent(this@PlaybackService, PlayerActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}
