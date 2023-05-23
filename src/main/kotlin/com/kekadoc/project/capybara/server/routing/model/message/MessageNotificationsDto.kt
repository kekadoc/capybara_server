package com.kekadoc.project.capybara.server.routing.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageNotificationsDto(
    @SerialName("email")
    val email: Boolean = false,
    @SerialName("sms")
    val sms: Boolean = false,
    @SerialName("app")
    val app: Boolean = false,
    @SerialName("messengers")
    val messengers: Boolean = false,
)