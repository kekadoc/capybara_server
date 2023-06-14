package com.kekadoc.project.capybara.server.routing.api.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRegistrationStatusResponseDto(
    @SerialName("info")
    val info: RegistrationRequestInfoDto,
)