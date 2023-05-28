package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

class MobileNotificationsRemoteDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : MobileNotificationsRemoteDataSource {

    override suspend fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
    ): String {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .setImage(imageUrl)
            .build()
        val message = Message.builder()
            .setNotification(notification)
            .setToken(pushToken)
            .build()
        return firebaseMessaging.send(message)
    }

}