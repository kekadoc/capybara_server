package com.kekadoc.project.capybara.server.data.source.api.notification.mobile.remote

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.kekadoc.project.capybara.server.common.extensions.flowOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

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