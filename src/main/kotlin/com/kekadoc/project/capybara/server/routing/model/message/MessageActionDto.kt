package com.kekadoc.project.capybara.server.routing.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageActionDto(
    @SerialName("id")
    val id: Long,
    @SerialName("text")
    val text: String,
)