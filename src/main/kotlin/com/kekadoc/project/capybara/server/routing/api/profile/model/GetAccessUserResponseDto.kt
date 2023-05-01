package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAccessUserResponseDto(
    @SerialName("read_profile")
    val readProfile: Boolean,
    @SerialName("sent_notification")
    val sentNotification: Boolean,
    @SerialName("contact_info")
    val contactInfo: Boolean,
)