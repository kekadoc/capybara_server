package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.source.message.MessageDataSource
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageDataSource: MessageDataSource
) : MessageRepository {
    
    override fun addMessage(message: Message): Flow<Unit> {
        return messageDataSource.addMessage(message)
    }
    
    override fun removeMessage(messageId: Identifier): Flow<Unit> {
        return messageDataSource.removeMessage(messageId)
    }
    
    override fun getMessage(messageId: Identifier): Flow<Message> {
        return messageDataSource.getMessage(messageId)
    }
    
    override fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>> {
        return messageDataSource.getMessagesByAuthorId(authorId)
    }
    
    override fun observeMessage(messageId: Identifier): Flow<Message> {
        return messageDataSource.observeMessage(messageId)
    }
    
}