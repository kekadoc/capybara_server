package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.routing.model.message.ReceivedMessagePreviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetReceivedMessageDto(
    @SerialName("message")
    val message: ReceivedMessagePreviewDto,
)