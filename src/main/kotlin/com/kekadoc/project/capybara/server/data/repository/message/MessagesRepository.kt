package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Range
import com.kekadoc.project.capybara.server.domain.model.message.*
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    fun createMessage(
        authorId: Identifier,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean,
        addresseeUsers: List<Identifier>,
        addresseeGroups: List<Identifier>,
        notifications: MessageNotifications?,
    ): Flow<Message>

    fun updateMessageStatus(
        messageId: Identifier,
        status: MessageStatus,
    ): Flow<Message>

    fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Flow<Message>

    fun removeMessage(messageId: Identifier): Flow<Unit>
    
    fun getMessage(messageId: Identifier): Flow<Message>

    fun getMessageInfo(messageId: Identifier): Flow<MessageInfo>

    fun getAddresseeUserInfo(messageId: Identifier, userId: Identifier): Flow<MessageForUser>

    fun getMessagesByAuthorId(
        authorId: Identifier,
        range: Range,
    ): Flow<List<Message>>

    fun getMessagesByAddresseeUserId(userId: Identifier, range: Range): Flow<List<Message>>

    //fun getMessagesByAddresseeGroupIds(groupIds: Set<Identifier>, range: Range): Flow<List<Message>>

    fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answerIds: List<Long>,
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