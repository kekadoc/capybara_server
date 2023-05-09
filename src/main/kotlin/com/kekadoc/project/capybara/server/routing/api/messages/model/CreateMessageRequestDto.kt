package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.data.source.network.model.MessageDto
import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequestDto(
    @SerialName("type")
    val type: MessageDto.Type,
    @SerialName("content")
    val content: MessageDto.Content,
    @SerialName("actions")
    val actions: MessageDto.Actions,
    @SerialName("addressee_users")
    val addresseeUsers: List<@Contextual Identifier>,
    @SerialName("addressee_groups")
    val addresseeGroups: List<@Contextual Identifier>,
    @SerialName("notifications")
    val notifications: MessageDto.Notifications? = null,
)