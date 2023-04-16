package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.MessageContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSentMessageRequest(
    @SerialName("content")
    val content: MessageContent = MessageContent.Empty,
    @SerialName("addresseeUsers")
    val addresseeUsers: List<Identifier>,
    @SerialName("addresseeGroups")
    val addresseeGroups: List<Identifier>,
)