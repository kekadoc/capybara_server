package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.model.MessageContent
import com.kekadoc.project.capybara.server.data.source.api.notification.NotificationsDataSource
import kotlinx.coroutines.flow.Flow

class MessagesRepositoryImpl(
    private val messagesDataSource: NotificationsDataSource,
    private val notificationsDataSource: NotificationsDataSource,
) : MessagesRepository {

    override fun getAll(): Flow<List<Message>> {
        return messagesDataSource.getAll()
    }

    override fun createMessage(
        authorId: Identifier,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: MessageContent,
    ): Flow<Message> = messagesDataSource.createMessage(
        authorId = authorId,
        addresseeGroups = addresseeGroups,
        addresseeUsers = addresseeUsers,
        content = content,
    )

    override fun updateMessage(
        messageId: Identifier,
        content: MessageContent
    ): Flow<Message> = messagesDataSource.updateMessage(
        messageId = messageId,
        content = content,
    )

    override fun updateMessageState(
        messageId: Identifier,
        state: Message.State,
    ): Flow<Message> = messagesDataSource.updateMessageState(
        messageId = messageId,
        state = state,
    )

    override fun removeMessage(messageId: Identifier): Flow<Unit> {
        return messagesDataSource.removeMessage(messageId)
    }
    
    override fun getMessage(messageId: Identifier): Flow<Message?> {
        return messagesDataSource.getMessage(messageId)
    }
    
    override fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>> {
        return messagesDataSource.getMessagesByAuthorId(authorId)
    }

    override fun getMessagesByAddresseeUserId(id: Identifier): Flow<List<Message>> {
        return messagesDataSource.getMessagesByAddresseeUserId(id)
    }

    override fun getMessagesByAddresseeGroupIds(id: Set<Identifier>): Flow<List<Message>> {
        return messagesDataSource.getMessagesByAddresseeGroupIds(id)
    }

    override fun observeMessage(messageId: Identifier): Flow<Message> {
        return messagesDataSource.observeMessage(messageId)
    }
    
}