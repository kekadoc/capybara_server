package com.kekadoc.project.capybara.server.data.source.api.notifications.email

import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User

interface EmailNotificationDataSource {

    suspend fun sentEmailNotification(
        message: Message,
        users: List<User>,
    )

    suspend fun receiveEmailAnswer(answerToken: Token): EmailNotificationAnswer?

}