package com.kekadoc.project.capybara.server.routing.api.notifications.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.source.network.model.NotificationDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSentNotificationRequest(
    @SerialName("type")
    val type: NotificationDto.Type,
    @SerialName("content")
    val content: NotificationDto.Content,
    @SerialName("addressee_users")
    val addresseeUsers: List<@Contextual Identifier>,
    @SerialName("addressee_groups")
    val addresseeGroups: List<@Contextual Identifier>,
)