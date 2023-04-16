package com.kekadoc.project.capybara.server.data.source.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationsDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : NotificationsDataSource {

    override fun sendNotification(
        pushToken: String,
        title: String,
        body: String,
        imageUrl: String,
    ): Flow<String> = flow {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .setImage(imageUrl)
            .build()
        val message = Message.builder()
            .setNotification(notification)
            .setToken(pushToken)
            .build()
        firebaseMessaging.send(message)
        //FirebaseMessaging.getInstance().send(message)
    }

}