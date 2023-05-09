package com.kekadoc.project.capybara.server.domain.intercator.notification

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.messages.model.*

interface MessagesInteractor {

    suspend fun getSentMessages(
        authToken: Token,
    ): GetSentNotificationsResponseDto

    suspend fun createMessage(
        authToken: Token,
        request: CreateMessageRequestDto,
    ): CreateNotificationResponseDto

    suspend fun getSentMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetSentMessageResponseDto

    suspend fun updateSentMessage(
        authToken: Token,
        messageId: Identifier,
        request: UpdateSentMessageRequestDto,
    ): UpdateSentMessageResponseDto

    suspend fun deleteSentMessage(
        authToken: Token,
        messageId: Identifier,
    )

    suspend fun getReceivedMessages(
        authToken: Token,
    ): GetReceivedMessagesDto

    suspend fun getReceivedMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetReceivedNotificationDto

    suspend fun setReceivedMessageAnswer(
        authToken: Token,
        messageId: Identifier,
        request: PostReceivedMessageAnswerRequestDto,
    )

    suspend fun setReceivedMessageNotify(
        authToken: Token,
        messageId: Identifier,
    )

    suspend fun setReadMessageNotify(
        authToken: Token,
        messageId: Identifier,
    )

}