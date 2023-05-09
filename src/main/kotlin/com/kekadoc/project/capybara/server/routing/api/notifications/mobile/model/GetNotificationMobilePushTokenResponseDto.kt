package com.kekadoc.project.capybara.server.routing.api.notifications.mobile.model

import com.kekadoc.project.capybara.server.domain.model.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationMobilePushTokenResponseDto(
    @SerialName("token")
    val token: Token?,
)