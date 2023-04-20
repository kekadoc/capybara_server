package com.kekadoc.project.capybara.server.data.source.api.notification

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.model.MessageContent
import kotlinx.coroutines.flow.Flow

interface NotificationsDataSource {

    fun getAll(): Flow<List<Message>>

    fun createMessage(
        authorId: Identifier,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: MessageContent,
    ): Flow<Message>

    fun updateMessage(
        messageId: Identifier,
        content: MessageContent,
    ): Flow<Message>

    fun updateMessageState(
        messageId: Identifier,
        state: Message.State,
    ): Flow<Message>

    fun removeMessage(messageId: Identifier): Flow<Unit>
    
    fun getMessage(messageId: Identifier): Flow<Message?>

    fun observeMessage(messageId: Identifier): Flow<Message>

    fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>>

    fun getMessagesByAddresseeUserId(id: Identifier): Flow<List<Message>>

    fun getMessagesByAddresseeGroupIds(ids: Set<Identifier>): Flow<List<Message>>

}