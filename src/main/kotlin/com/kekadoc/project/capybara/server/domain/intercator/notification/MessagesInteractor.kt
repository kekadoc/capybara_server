package com.kekadoc.project.capybara.server.domain.intercator.notification

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.messages.model.*
import com.kekadoc.project.capybara.server.routing.model.RangeDto

interface MessagesInteractor {

    suspend fun getReceivedMessages(
        authToken: Token,
        range: RangeDto,
    ): GetReceivedMessagesResponseDto

    suspend fun getReceivedMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetReceivedNotificationDto

    suspend fun setReceivedMessageAnswer(
        authToken: Token,
        messageId: Identifier,
        request: UpdateReceivedMessageAnswerRequestDto,
    )

    suspend fun setReceivedMessageNotify(
        authToken: Token,
        messageId: Identifier,
    )

    suspend fun setReadMessageNotify(
        authToken: Token,
        messageId: Identifier,
    )

    suspend fun getSentMessages(
        authToken: Token,
        range: RangeDto,
    ): GetSentMessagesResponseDto

    suspend fun getSentMessage(
        authToken: Token,
        messageId: Identifier,
    ): GetSentMessageResponseDto

    suspend fun createMessage(
        authToken: Token,
        request: CreateMessageRequestDto,
    ): CreateNotificationResponseDto

    suspend fun deleteSentMessage(
        authToken: Token,
        messageId: Identifier,
    )

}