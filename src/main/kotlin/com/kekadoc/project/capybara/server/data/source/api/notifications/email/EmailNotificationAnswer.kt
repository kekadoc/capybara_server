package com.kekadoc.project.capybara.server.data.source.api.notifications.email

import com.kekadoc.project.capybara.server.domain.model.Identifier

data class EmailNotificationAnswer(
    val userId: Identifier,
    val messageId: Identifier,
    val answer: String,
)