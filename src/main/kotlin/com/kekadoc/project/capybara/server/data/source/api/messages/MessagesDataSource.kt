package com.kekadoc.project.capybara.server.data.source.api.messages

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.common.Range
import com.kekadoc.project.capybara.server.domain.model.message.*

interface MessagesDataSource {

    suspend fun createMessage(
        authorId: Identifier,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean = false,
        addresseeUsers: List<Identifier>,
        addresseeGroups: Map<Identifier, List<Identifier>?>,
        notifications: MessageNotifications,
    ): Message

    suspend fun updateMessageStatus(
        messageId: Identifier,
        status: MessageStatus,
    ): Message

    suspend fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Message

    suspend fun removeMessage(messageId: Identifier): Message

    suspend fun getMessage(messageId: Identifier): Message

    suspend fun getAddresseeUserInfo(
        messageId: Identifier,
        userId: Identifier,
    ): MessageForUser

    suspend fun getMessageInfo(messageId: Identifier): MessageInfo

    suspend fun getMessagesByAuthorId(authorId: Identifier, range: Range): List<Message>

    suspend fun getMessagesByAddresseeUserId(userId: Identifier, range: Range): List<Message>

    suspend fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answerIds: List<Long>,
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