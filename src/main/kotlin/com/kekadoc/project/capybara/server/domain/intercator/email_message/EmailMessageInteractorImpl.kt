package com.kekadoc.project.capybara.server.domain.intercator.email_message

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.message.MessagesRepository
import com.kekadoc.project.capybara.server.data.repository.notification.email.EmailNotificationRepository
import io.ktor.http.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class EmailMessageInteractorImpl(
    private val emailNotificationRepository: EmailNotificationRepository,
    private val messagesRepository: MessagesRepository,
) : EmailMessageInteractor {

    override suspend fun sentAnswerFromEmail(answerToken: String) {
        emailNotificationRepository.receiveEmailAnswer(answerToken)
            .map { result -> result ?: throw HttpException(HttpStatusCode.NotFound) }
            .flatMapConcat { result ->
                messagesRepository.setReceivedMessageAnswer(
                    messageId = result.messageId,
                    userId = result.userId,
                    answer = result.answer,
                )
            }
            .collect()
    }

}