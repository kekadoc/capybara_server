package com.kekadoc.project.capybara.server.data.source.network.model

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