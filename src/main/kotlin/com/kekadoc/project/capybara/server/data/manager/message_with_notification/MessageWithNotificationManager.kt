package com.kekadoc.project.capybara.server.data.manager.message_with_notification

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageAction
import com.kekadoc.project.capybara.server.domain.model.message.MessageNotifications
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface MessageWithNotificationManager {

    fun sentMessage(
        author: User,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean = false,
        addresseeUsers: List<Identifier>,
        addresseeGroups: Map<Identifier, List<Identifier>?>,
        notifications: MessageNotifications? = null,
    ): Flow<Message>

}