package com.kekadoc.project.capybara.server.data.source.api.notifications.email

data class EmailNotificationConfig(
    val tokenSecret: String,
    val hostName: String,
    val smtpPort: Int,
    val username: String,
    val password: String,
    val fromEmail: String,
    val subject: String,
)