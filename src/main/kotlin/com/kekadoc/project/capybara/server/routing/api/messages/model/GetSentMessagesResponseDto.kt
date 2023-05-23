package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.routing.model.message.SentMessagePreviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSentMessagesResponseDto(
    @SerialName("items")
    val items: List<SentMessagePreviewDto>,
)