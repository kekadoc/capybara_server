package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.MessageInfo
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    fun createMessage(
        authorId: Identifier,
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications,
    ): Flow<Message>

    fun updateMessage(
        messageId: Identifier,
        content: Message.Content,
    ): Flow<Message>

    fun updateMessageStatus(
        messageId: Identifier,
        status: MessageInfo.Status,
    ): Flow<Message>

    fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Flow<Message>

    fun removeMessage(messageId: Identifier): Flow<Unit>
    
    fun getMessage(messageId: Identifier): Flow<Message>

    fun getMessageInfo(messageId: Identifier): Flow<MessageInfo>

    fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>>

    fun getMessagesByAddresseeUserId(userId: Identifier): Flow<List<Message>>

    fun getMessagesByAddresseeGroupIds(groupIds: Set<Identifier>): Flow<List<Message>>

    fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answer: String,
    ): Flow<Message>

    fun setReceivedMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Flow<Message>

    fun setReadMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Flow<Message>

}