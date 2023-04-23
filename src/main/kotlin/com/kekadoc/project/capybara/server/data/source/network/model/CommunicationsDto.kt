package com.kekadoc.project.capybara.server.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CommunicationsDto(
    val values: List<CommunicationDto>,
)
