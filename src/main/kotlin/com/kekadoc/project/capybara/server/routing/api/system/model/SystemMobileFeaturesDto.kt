package com.kekadoc.project.capybara.server.routing.api.system.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SystemMobileFeaturesDto(
    @SerialName("change_profile")
    val changeProfile: Boolean,
    @SerialName("reset_profile")
    val resetPassword: Boolean,
    @SerialName("registration")
    val registration: Boolean,
    @SerialName("available_communications")
    val availableCommunications: List<String>,
)