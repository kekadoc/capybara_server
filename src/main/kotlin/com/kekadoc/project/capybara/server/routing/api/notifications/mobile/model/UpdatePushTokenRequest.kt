package com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenRequest(
    @SerialName("token")
    val token: String,
)