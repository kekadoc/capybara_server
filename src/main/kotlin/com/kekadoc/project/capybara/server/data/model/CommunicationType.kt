package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CommunicationType {
    @SerialName("TELEPHONE")
    TELEPHONE,
    @SerialName("TELEGRAM")
    TELEGRAM,
    @SerialName("WHATS_APP")
    WHATS_APP,
    @SerialName("VIBER")
    VIBER,
    @SerialName("EMAIL")
    EMAIL,
    @SerialName("ANOTHER")
    ANOTHER,
}