package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.data.source.network.model.CommunicationsDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateContactRequest(
    val communications: CommunicationsDto,
)