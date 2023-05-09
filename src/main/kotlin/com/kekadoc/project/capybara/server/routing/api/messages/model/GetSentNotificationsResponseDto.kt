package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.data.source.network.model.MessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSentNotificationsResponseDto(
    @SerialName("items")
    val items: List<MessageDto>,
)