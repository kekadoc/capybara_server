package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class MobileNotificationsRemoteDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : MobileNotificationsRemoteDataSource {

    override suspend fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
    ): String {
        println("___sendNotification $title $body $imageUrl $pushToken")
//        val notification = Notification.builder()
//            .setTitle(title)
//            .setBody(body)
//            .setImage(imageUrl)
//            .build()
//        val message = Message.builder()
//            .setNotification(notification)
//            .setToken(pushToken)
//            .build()
//        return firebaseMessaging.send(message)
        return UUID.randomUUID().toString()
    }

}