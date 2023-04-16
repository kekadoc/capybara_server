package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.data.model.Contact
import kotlinx.serialization.Serializable

@Serializable
data class GetContactResponse(
    val contact: Contact,
)