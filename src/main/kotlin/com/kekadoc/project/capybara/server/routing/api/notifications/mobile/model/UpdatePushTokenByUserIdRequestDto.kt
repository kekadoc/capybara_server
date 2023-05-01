package com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenByUserIdRequestDto(
    @Contextual
    @SerialName("user_id")
    val userId: Identifier,
    @SerialName("token")
    val token: Token?,
)