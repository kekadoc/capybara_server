package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCommunicationsRequestDto(
    @SerialName("values")
    val values: Map<String, String>,
)