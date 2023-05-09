package com.kekadoc.project.capybara.server.data.manager.message_with_notification

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageWithNotificationManager {

    fun sentMessage(
        authorId: Identifier,
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications,
    ): Flow<Message>

}