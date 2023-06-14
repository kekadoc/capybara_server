package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.routing.model.contact.ContactDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllPublicContactsResponseDto(
    @SerialName("items")
    val items: List<ContactDto>,
)