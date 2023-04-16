package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.data.model.MessageContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSentMessageRequest(
    @SerialName("content")
    val content: MessageContent = MessageContent.Empty,
)