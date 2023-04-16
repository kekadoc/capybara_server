package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.data.model.CommunicationType
import kotlinx.serialization.Serializable

@Serializable
data class CreateContactRequest(
    val userContactId: String,
    val communications: Map<CommunicationType, String>,
)