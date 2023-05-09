package com.kekadoc.project.capybara.server.data.source.api.messages

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.MessageInfo

interface MessagesDataSource {

    suspend fun createMessage(
        authorId: Identifier,
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications,
    ): Message

    suspend fun updateMessage(
        messageId: Identifier,
        content: Message.Content,
    ): Message

    suspend fun updateMessageStatus(
        messageId: Identifier,
        status: MessageInfo.Status,
    ): Message

    suspend fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Message

    suspend fun removeMessage(messageId: Identifier): Message

    suspend fun getMessage(messageId: Identifier): Message

    suspend fun getMessageInfo(messageId: Identifier): MessageInfo

    suspend fun getMessagesByAuthorId(authorId: Identifier): List<Message>

    suspend fun getMessagesByAddresseeUserId(userId: Identifier): List<Message>

    suspend fun getMessagesByAddresseeGroupIds(groupIds: Set<Identifier>): List<Message>

    suspend fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answer: String,
    ): Message

    suspend fun setReceivedMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Message

    suspend fun setReadMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Message

}