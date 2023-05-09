package com.kekadoc.project.capybara.server.data.repository.notification.email

import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationAnswer
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import kotlinx.coroutines.flow.Flow

interface EmailNotificationRepository {

    suspend fun sentEmailNotification(message: Message, users: List<User>): Flow<Unit>

    suspend fun receiveEmailAnswer(answerToken: Token): Flow<EmailNotificationAnswer?>

}