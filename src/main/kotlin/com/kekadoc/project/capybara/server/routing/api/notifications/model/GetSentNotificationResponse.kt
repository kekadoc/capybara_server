package com.kekadoc.project.capybara.server.routing.api.notifications.model

import com.kekadoc.project.capybara.server.data.source.network.model.NotificationInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSentNotificationResponse(
    @SerialName("info")
    val notification: NotificationInfoDto,
)