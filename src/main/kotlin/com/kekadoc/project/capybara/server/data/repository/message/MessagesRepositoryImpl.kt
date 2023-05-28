package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.data.source.api.messages.MessagesDataSource
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Range
import com.kekadoc.project.capybara.server.domain.model.message.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MessagesRepositoryImpl(
    private val messagesDataSource: MessagesDataSource,
) : MessagesRepository {

    override fun createMessage(
        authorId: Identifier,
        type: MessageType,
        title: String?,
        text: String,
        actions: List<MessageAction>?,
        isMultiAction: Boolean,
        addresseeUsers: List<Identifier>,
        addresseeGroups: Map<Identifier, List<Identifier>?>,
        notifications: MessageNotifications?,
    ): Flow<Message> = flowOf {
        messagesDataSource.createMessage(
            authorId = authorId,
            type = type,
            title = title,
            text = text,
            actions = actions,
            isMultiAction = isMultiAction,
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            notifications = notifications ?: MessageNotifications.Default,
        )
    }

    override fun updateMessageStatus(
        messageId: Identifier,
        status: MessageStatus,
    ): Flow<Message> = flowOf {
        messagesDataSource.updateMessageStatus(
            messageId = messageId,
            status = status,
        )
    }

    override fun updateMessageUserInfo(
        messageId: Identifier,
        info: MessageInfo.FromUserInfo,
    ): Flow<Message> = flowOf {
        messagesDataSource.updateMessageUserInfo(
            messageId = messageId,
            info = info,
        )
    }

    override fun removeMessage(
        messageId: Identifier,
    ): Flow<Unit> = flowOf {
        messagesDataSource.removeMessage(
            messageId = messageId,
        )
    }

    override fun getMessage(
        messageId: Identifier,
    ): Flow<Message> = flowOf {
        messagesDataSource.getMessage(
            messageId = messageId,
        )
    }

    override fun getMessageInfo(messageId: Identifier): Flow<MessageInfo> = flowOf {
        messagesDataSource.getMessageInfo(
            messageId = messageId,
        )
    }

    override fun getAddresseeUserInfo(
        messageId: Identifier,
        userId: Identifier,
    ): Flow<MessageForUser> = flowOf {
        messagesDataSource.getAddresseeUserInfo(
            messageId = messageId,
            userId = userId,
        )
    }

    override fun getMessagesByAuthorId(
        authorId: Identifier,
        range: Range,
    ): Flow<List<Message>> = flowOf {
        messagesDataSource.getMessagesByAuthorId(
            authorId = authorId,
            range = range,
        )
    }

    override fun getMessagesByAddresseeUserId(
        userId: Identifier,
        range: Range,
    ): Flow<List<Message>> = flowOf {
        messagesDataSource.getMessagesByAddresseeUserId(
            userId = userId,
            range = range,
        )
    }

    override fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answerIds: List<Long>,
    ): Flow<Message> = flowOf {
        messagesDataSource.setReceivedMessageAnswer(
            messageId = messageId,
            userId = userId,
            answerIds = answerIds,
        )
    }

    override fun setReceivedMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Flow<Message> = flowOf {
        messagesDataSource.setReceivedMessageNotify(
            messageId = messageId,
            userId = userId,
        )
    }

    override fun setReadMessageNotify(
        messageId: Identifier,
        userId: Identifier,
    ): Flow<Message> = flowOf {
        messagesDataSource.setReadMessageNotify(
            messageId = messageId,
            userId = userId,
        )
    }

}