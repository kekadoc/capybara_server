package com.kekadoc.project.capybara.server.routing.api.notifications.model

import com.kekadoc.project.capybara.server.data.source.network.model.NotificationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSentMessageRequest(
    @SerialName("content")
    val content: NotificationDto.Content,
)