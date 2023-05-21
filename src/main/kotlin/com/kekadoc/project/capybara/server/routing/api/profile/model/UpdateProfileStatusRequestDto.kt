package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileStatusRequestDto(
    @SerialName("status")
    val status: String,
)