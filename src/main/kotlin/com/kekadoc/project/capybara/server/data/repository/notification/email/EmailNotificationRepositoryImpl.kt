package com.kekadoc.project.capybara.server.data.repository.notification.email

import com.kekadoc.project.capybara.server.data.service.email.EmailDataService
import com.kekadoc.project.capybara.server.data.service.email.EmailNotificationAnswer
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.message.MessageType
import com.kekadoc.project.capybara.server.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EmailNotificationRepositoryImpl(
    private val emailDataService: EmailDataService,
) : EmailNotificationRepository {

    override suspend fun sentEmailNotification(
        message: Message,
        author: User,
        users: List<User>,
    ): Flow<Unit> = flowOf {
        when (message.type) {
            MessageType.DEFAULT -> {}
            MessageType.INFO -> emailDataService.sentEmailInfoNotification(
                message = message,
                author = author,
                users = users,
            )
            MessageType.VOTE -> emailDataService.sentEmailVoteNotification(
                message = message,
                author = author,
                users = users,
            )
        }
    }

    override suspend fun receiveEmailAnswer(
        answerToken: Token,
    ): Flow<EmailNotificationAnswer?> = flowOf {
        emailDataService.receiveEmailAnswer(answerToken)
    }

}