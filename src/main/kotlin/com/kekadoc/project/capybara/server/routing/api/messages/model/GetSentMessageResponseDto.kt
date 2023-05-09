package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.data.source.network.model.MessageInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSentMessageResponseDto(
    @SerialName("info")
    val messageInfo: MessageInfoDto,
)