package com.kekadoc.project.capybara.server.routing.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MessageTypeDto {
    @SerialName("DEFAULT")
    DEFAULT,
    @SerialName("INFO")
    INFO,
    @SerialName("VOTE")
    VOTE
}