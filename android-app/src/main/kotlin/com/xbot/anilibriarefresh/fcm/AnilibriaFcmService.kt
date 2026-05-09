package com.xbot.anilibriarefresh.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.xbot.anilibriarefresh.notifications.NotificationHelper

class AnilibriaFcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        sendTokenToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification
        val data = message.data

        val title = notification?.title ?: data[KEY_TITLE]
        val body = notification?.body ?: data[KEY_BODY]
        val releaseId = data[KEY_RELEASE_ID]

        if (title.isNullOrBlank() && body.isNullOrBlank()) return

        NotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            message = body,
            releaseId = releaseId,
        )
    }

    private fun sendTokenToServer(token: String) {
        // TODO: Persist the token and forward it to the backend once the API is available.
    }

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val KEY_RELEASE_ID = "release_id"
    }
}
