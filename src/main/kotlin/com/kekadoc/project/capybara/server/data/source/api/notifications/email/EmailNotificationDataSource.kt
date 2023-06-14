package com.kekadoc.project.capybara.server.data.source.api.notifications.email

import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.user.User
import com.kekadoc.project.capybara.server.domain.model.message.Message

interface EmailNotificationDataSource {

    suspend fun sentEmailNotification(
        message: Message,
        users: List<User>,
    )

    suspend fun receiveEmailAnswer(answerToken: Token): EmailNotificationAnswer?

}