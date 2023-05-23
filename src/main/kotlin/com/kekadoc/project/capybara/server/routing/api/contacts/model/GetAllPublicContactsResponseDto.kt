package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.routing.model.ContactDto
import kotlinx.serialization.Serializable

@Serializable
data class GetAllPublicContactsResponseDto(
    val items: List<ContactDto>,
)