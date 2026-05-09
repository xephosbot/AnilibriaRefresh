package com.xbot.anilibriarefresh

import android.app.Application
import android.content.pm.ApplicationInfo
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.xbot.anilibriarefresh.fcm.FcmTopics
import com.xbot.anilibriarefresh.notifications.NotificationHelper
import com.xbot.sharedapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinConfiguration

class AnilibriaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
        subscribeToFcmTopics()
        logFcmTokenInDebug()

        initKoin(
            config = koinConfiguration {
                androidContext(this@AnilibriaApplication)
            }
        )
    }

    private fun subscribeToFcmTopics() {
        Firebase.messaging.subscribeToTopic(FcmTopics.ALL_USERS)
    }

    private fun logFcmTokenInDebug() {
        if (!isDebuggable()) return
        Firebase.messaging.token.addOnSuccessListener { token ->
            Log.d(FCM_LOG_TAG, "Token: $token")
        }
    }

    private fun isDebuggable(): Boolean {
        return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    private companion object {
        const val FCM_LOG_TAG = "FCM"
    }
}
