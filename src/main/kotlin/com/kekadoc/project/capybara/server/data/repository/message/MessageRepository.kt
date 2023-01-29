package com.kekadoc.project.capybara.server.data.repository.message

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    
    fun addMessage(message: Message): Flow<Unit>
    
    fun removeMessage(messageId: Identifier): Flow<Unit>
    
    fun getMessage(messageId: Identifier): Flow<Message>
    
    fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>>
    
    fun observeMessage(messageId: Identifier): Flow<Message>
    
}