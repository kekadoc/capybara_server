package com.kekadoc.project.capybara.server.data.source.message

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.model.MessageContent
import kotlinx.coroutines.flow.*

class MessageDataSourceImpl(
    database: FirebaseDatabase,
) : MessagesDataSource {

    private val messages = database.getReference("/messages")

    override fun getAll(): Flow<List<Message>> = flowOf {
        messages
            .getAll<Message>()
            .values.toList()
            .filterNotNull()
    }

    override fun createMessage(
        authorId: Identifier,
        addresseeGroups: Set<Identifier>,
        addresseeUsers: Set<Identifier>,
        content: MessageContent,
    ): Flow<Message> {
        return flow {
            val newMessageDocument = messages.push()
            val message = Message(
                id = newMessageDocument.key,
                authorId = authorId,
                addresseeGroups = addresseeGroups.toList(),
                addresseeUsers = addresseeUsers.toList(),
                content = content,
                state = Message.State(
                    status = Message.State.Status.SENT, // TODO:
                )
            )
            newMessageDocument.set(message)
            emit(message)
        }
    }

    override fun updateMessage(
        messageId: Identifier,
        content: MessageContent,
    ): Flow<Message> {
        return getMessage(messageId)
            .map { message -> message ?: throw IllegalStateException("Message not found") }
            .map { currentMessage ->
                currentMessage.copy(
                    content = content,
                )
            }
            .onEach { newMessage -> messages.child(messageId).set(newMessage) }
    }

    override fun updateMessageState(
        messageId: Identifier,
        state: Message.State,
    ): Flow<Message> {
        return getMessage(messageId)
            .map { message -> message ?: throw IllegalStateException("Message not found") }
            .map { currentMessage ->
                currentMessage.copy(
                    state = state,
                )
            }
            .onEach { newMessage -> messages.child(messageId).set(newMessage) }
    }

    override fun removeMessage(
        messageId: Identifier,
    ): Flow<Unit> {
        return flowOf {
            messages.child(messageId).remove()
        }
    }
    
    override fun getMessage(
        messageId: Identifier,
    ): Flow<Message?> {
        return flow {
            val message = messages.child(messageId).get<Message?>()
            emit(message)
        }
    }
    
    override fun getMessagesByAuthorId(
        authorId: Identifier,
    ): Flow<List<Message>> {
        return flowOf {
            messages
                .orderByChild("authorId")
                .equalTo(authorId)
                .getAll<Message>()
                .values
                .filterNotNull()
                .toList()
        }
    }

    override fun getMessagesByAddresseeUserId(id: Identifier): Flow<List<Message>> = flowOf {
        messages
            .getAll<Message>()
            .values.toList()
            .filterNotNull()
            .filter { message ->
                message.addresseeUsers.any { userId ->
                    id == userId
                }
            }
    }

    override fun getMessagesByAddresseeGroupIds(ids: Set<Identifier>): Flow<List<Message>> = flowOf {
        messages
            .getAll<Message>()
            .values.toList()
            .filterNotNull()
            .filter { message ->
                message.addresseeGroups.any { userId ->
                    ids.any { it == userId }
                }
            }
    }

    override fun observeMessage(
        messageId: Identifier,
    ): Flow<Message> {
        return messages.child(messageId).observeValue<Message>().filterNotNull()
    }
    
}