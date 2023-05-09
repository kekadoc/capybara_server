package com.kekadoc.project.capybara.server.data.repository.notification.email

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationAnswer
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationDataSource
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import kotlinx.coroutines.flow.Flow

class EmailNotificationRepositoryImpl(
    private val emailNotificationDataSource: EmailNotificationDataSource,
) : EmailNotificationRepository {

    override suspend fun sentEmailNotification(
        message: Message,
        users: List<User>,
    ): Flow<Unit> = flowOf {
        emailNotificationDataSource.sentEmailNotification(
            message = message,
            users = users,
        )
    }

    override suspend fun receiveEmailAnswer(
        answerToken: Token,
    ): Flow<EmailNotificationAnswer?> = flowOf {
        emailNotificationDataSource.receiveEmailAnswer(answerToken)
    }

}