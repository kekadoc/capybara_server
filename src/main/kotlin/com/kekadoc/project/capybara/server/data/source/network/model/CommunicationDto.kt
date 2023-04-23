package com.kekadoc.project.capybara.server.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CommunicationDto(
    val type: Type,
    val value: String,
) {

    enum class Type {
        PHONE,
        EMAIL,
        VIBER,
        WHATS_APP,
        TELEGRAM,
        UNKNOWN,
    }

}