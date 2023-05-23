package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.routing.model.message.SentMessageInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSentMessageResponseDto(
    @SerialName("message")
    val message: SentMessageInfoDto,
)