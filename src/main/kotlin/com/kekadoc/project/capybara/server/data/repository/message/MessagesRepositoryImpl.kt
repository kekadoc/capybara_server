package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.source.api.messages.MessagesDataSource
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.MessageInfo
import kotlinx.coroutines.flow.Flow

class MessagesRepositoryImpl(
    private val messagesDataSource: MessagesDataSource,
) : MessagesRepository {

    override fun createMessage(
        authorId: Identifier,
        type: Message.Type,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: Message.Content,
        actions: Message.Actions?,
        notifications: Message.Notifications
    ): Flow<Message> = flowOf {
        messagesDataSource.createMessage(
            authorId = authorId,
            type = type,
            addresseeGroups = addresseeGroups,
            addresseeUsers = addresseeUsers,
            content = content,
            actions = actions,
            notifications = notifications,
        )
    }

    override fun updateMessage(
        messageId: Identifier,
        content: Message.Content,
    ): Flow<Message> = flowOf {
        messagesDataSource.updateMessage(
            messageId = messageId,
            content = content,
        )
    }

    override fun updateMessageStatus(
        messageId: Identifier,
        status: MessageInfo.Status,
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

    override fun getMessagesByAuthorId(
        authorId: Identifier,
    ): Flow<List<Message>> = flowOf {
        messagesDataSource.getMessagesByAuthorId(
            authorId = authorId,
        )
    }

    override fun getMessagesByAddresseeUserId(
        userId: Identifier,
    ): Flow<List<Message>> = flowOf {
        messagesDataSource.getMessagesByAddresseeUserId(
            userId = userId,
        )
    }

    override fun getMessagesByAddresseeGroupIds(
        groupIds: Set<Identifier>,
    ): Flow<List<Message>> = flowOf {
        messagesDataSource.getMessagesByAddresseeGroupIds(
            groupIds = groupIds,
        )
    }

    override fun setReceivedMessageAnswer(
        messageId: Identifier,
        userId: Identifier,
        answer: String,
    ): Flow<Message> = flowOf {
        messagesDataSource.setReceivedMessageAnswer(
            messageId = messageId,
            userId = userId,
            answer = answer,
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