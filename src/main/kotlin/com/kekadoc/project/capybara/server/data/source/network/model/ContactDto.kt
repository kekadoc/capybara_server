package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class ContactDto(
    val id: Identifier,
    val profile: ProfileDto,
    val communications: CommunicationsDto,
)