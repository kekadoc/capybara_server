package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String = emptyString(),
    val author: Person = Person.Empty,
    val content: MessageContent = MessageContent.Empty,
    val state: State = State.Empty
) {
    
    @Serializable
    data class State(
        val status: Status = Status.UNDEFINED,
        val readCounter: Int = 0,
        val receiveCounter: Int = 0
    ) {
        
        companion object {
            val Empty: State = State()
        }
        
        @Serializable
        enum class Status {
            UNDEFINED,
            RECEIVED,
            SENT,
            REJECTED,
            FAILED
        }
    }
}