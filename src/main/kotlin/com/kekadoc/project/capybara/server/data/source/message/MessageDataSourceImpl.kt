package com.kekadoc.project.capybara.server.data.source.message

import com.google.firebase.database.FirebaseDatabase
import com.kekadoc.project.capybara.server.common.extensions.*
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Message
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

@OptIn(FlowPreview::class)
class MessageDataSourceImpl : MessageDataSource {
    
    private val database = FirebaseDatabase.getInstance()
    private val messages = database.getReference("/messages")
    
    override fun addMessage(message: Message): Flow<Unit> {
        return flowOf {
            messages.child(message.id).set(message)
        }
    }
    
    override fun removeMessage(messageId: Identifier): Flow<Unit> {
        return flowOf {
            messages.child(messageId).remove()
        }
    }
    
    override fun getMessage(messageId: Identifier): Flow<Message> {
        return flow {
            val message = messages.child(messageId).get<Message>()
            if (message != null) emit(message)
        }
    }
    
    override fun getMessagesByAuthorId(authorId: Identifier): Flow<List<Message>> {
        return flowOf {
            messages
                .orderByChild("author/id")
                .equalTo(authorId)
                .getAll<Message>()
                .values
                .filterNotNull()
                .toList()
        }
    }
    
    override fun observeMessage(messageId: Identifier): Flow<Message> {
        return messages.child(messageId).observeValue<Message>().filterNotNull()
    }
    
}