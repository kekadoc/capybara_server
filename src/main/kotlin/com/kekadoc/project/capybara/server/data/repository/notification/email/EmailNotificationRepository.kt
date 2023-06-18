package com.kekadoc.project.capybara.server.data.repository.notification.email

import com.kekadoc.project.capybara.server.data.service.email.EmailNotificationAnswer
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.message.Message
import com.kekadoc.project.capybara.server.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface EmailNotificationRepository {

    suspend fun sentEmailNotification(
        message: Message,
        author: User,
        users: List<User>,
    ): Flow<Unit>

    suspend fun receiveEmailAnswer(answerToken: Token): Flow<EmailNotificationAnswer?>

}