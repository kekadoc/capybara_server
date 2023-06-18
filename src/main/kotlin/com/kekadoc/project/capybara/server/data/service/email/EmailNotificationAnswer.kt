package com.kekadoc.project.capybara.server.data.service.email

import com.kekadoc.project.capybara.server.domain.model.Identifier

data class EmailNotificationAnswer(
    val userId: Identifier,
    val messageId: Identifier,
    val answerId: Long,
)