package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccessGroupResponseDto(
    @SerialName("read_info")
    val readInfo: Boolean,
    @SerialName("read_members")
    val readMembers: Boolean,
    @SerialName("sent_notification")
    val sentNotification: Boolean,
)