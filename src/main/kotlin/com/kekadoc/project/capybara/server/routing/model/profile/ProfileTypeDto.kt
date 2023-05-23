package com.kekadoc.project.capybara.server.routing.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProfileTypeDto {
    @SerialName("USER")
    USER,
    @SerialName("SPEAKER")
    SPEAKER,
    @SerialName("ADMIN")
    ADMIN,
}