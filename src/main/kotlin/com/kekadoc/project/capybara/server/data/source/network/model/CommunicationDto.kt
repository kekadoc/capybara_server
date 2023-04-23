package com.kekadoc.project.capybara.server.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CommunicationDto(
    val name: String,
    val value: String,
)