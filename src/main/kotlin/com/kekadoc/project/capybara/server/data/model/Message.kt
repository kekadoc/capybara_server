package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("id")
    val id: String = emptyString(),
    @SerialName("authorId")
    val authorId: Identifier = emptyString(),
    @SerialName("addresseeGroups")
    val addresseeGroups: List<Identifier>,
    @SerialName("addresseeUsers")
    val addresseeUsers: List<Identifier>,
    @SerialName("content")
    val content: MessageContent = MessageContent.Empty,
    @SerialName("state")
    val state: State = State.Empty
) {
    
    @Serializable
    data class State(
        @SerialName("status")
        val status: Status = Status.UNDEFINED,
        @SerialName("readCounter")
        val readCounter: Int = 0,
        @SerialName("receiveCounter")
        val receiveCounter: Int = 0
    ) {
        
        companion object {
            val Empty: State = State()
        }
        
        @Serializable
        enum class Status {
            UNDEFINED,
            /**
             * Принято сервером
             */
            RECEIVED,
            /**
             * Отправлено
             */
            SENT,
            /**
             * Отменено
             */
            CANCELED,
            /**
             * Заблокировано сервером
             */
            REJECTED,
            /**
             * Ошибка отправки
             */
            FAILED
        }
    }
}