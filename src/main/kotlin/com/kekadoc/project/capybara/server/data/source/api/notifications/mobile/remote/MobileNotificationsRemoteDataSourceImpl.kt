package com.kekadoc.project.capybara.server.data.source.api.notifications.mobile.remote

import com.google.firebase.messaging.FirebaseMessaging
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.user.officialName
import com.kekadoc.project.capybara.server.domain.model.user.profile
import com.google.firebase.messaging.Message as FirebaseMessage

class MobileNotificationsRemoteDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : MobileNotificationsRemoteDataSource {

    override suspend fun sendNotification(
        pushToken: String,
        message: Message,
        author: User,
    ): String {
        val fbMessage = FirebaseMessage.builder()
            .putData("id", message.id.toString())
            .putData("title", message.title)
            .putData("text", message.text)
            .putData("author", author.profile.officialName)
            .putData("type", message.type.name)
            .apply {
                if (message.type == MessageType.INFO) {
                    val actionButtons = message.actions.take(3)
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
            }
            .setToken(pushToken)
            .build()
        return firebaseMessaging.send(fbMessage)
    }

}