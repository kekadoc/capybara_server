package com.kekadoc.project.capybara.server.routing.api.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RegistrationStatusDto {
    @SerialName("WAIT_EMAIL_CONFIRMING")
    WAIT_EMAIL_CONFIRMING,
    @SerialName("WAIT_APPROVING")
    WAIT_APPROVING,
    @SerialName("COMPLETED")
    COMPLETED,
    @SerialName("REJECTED")
    REJECTED,
    @SerialName("CANCELLED")
    CANCELLED,
}