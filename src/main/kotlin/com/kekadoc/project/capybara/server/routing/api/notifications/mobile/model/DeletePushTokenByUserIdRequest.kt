package com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeletePushTokenByUserIdRequest(
    @SerialName("user_id")
    val userId: String,
)