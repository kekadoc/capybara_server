package com.kekadoc.project.capybara.server.intercator.messages

import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.routing.api.messages.model.*
import kotlinx.coroutines.flow.Flow

interface MessagesInteractor {

    suspend fun getSentMessages(
        authToken: String,
    ): GetSentMessagesResponse

    suspend fun createSentMessage(
        authToken: String,
        request: CreateSentMessageRequest,
    ): CreateSentMessageResponse

    suspend fun getSentMessage(
        authToken: String,
        messageId: String,
    ): GetSentMessageResponse

    suspend fun updateSentMessage(
        authToken: String,
        messageId: String,
        request: UpdateSentMessageRequest,
    ): UpdateSentMessageResponse

    suspend fun deleteSentMessage(
        authToken: String,
        messageId: String,
    )

    fun observeSentMessageState(
        authToken: String,
        messageId: String,
    ): Flow<Message.State>


    suspend fun getReceivedMessages(
        authToken: String,
    ): GetReceivedMessages

    suspend fun getReceivedMessage(
        authToken: String,
        messageId: String,
    ): GetReceivedMessage

    suspend fun setReceivedMessageAnswer(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageAnswerRequest,
    )

    suspend fun setReceivedMessageNotify(
        authToken: String,
        messageId: String,
        request: PostReceivedMessageNotifyRequest,
    )

}