package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction

class MobileNotificationsRemoteDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : MobileNotificationsRemoteDataSource {

    override suspend fun sendNotification(
        pushToken: String,
        title: String?,
        body: String,
        imageUrl: String?,
        actions: List<MessageAction>,
    ): String {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .setImage(imageUrl)
            .build()
        val message = Message.builder()
            .putData("title", title)
            .putData("text", body)
            .apply {
                val actionButtons = actions.take(3)
                val action1 = actionButtons.getOrNull(0) ?: return@apply
                putData("action_1_id", action1.id.toString())
                putData("action_1_text", action1.text)
                val action2 = actionButtons.getOrNull(1) ?: return@apply
                putData("action_2_id", action2.id.toString())
                putData("action_2_text", action2.text)
                val action3 = actionButtons.getOrNull(2) ?: return@apply
                putData("action_3_id", action3.id.toString())
                putData("action_3_text", action3.text)
            }
            //.setNotification(notification)
            .setToken(pushToken)
            .build()
        return firebaseMessaging.send(message)
    }

}