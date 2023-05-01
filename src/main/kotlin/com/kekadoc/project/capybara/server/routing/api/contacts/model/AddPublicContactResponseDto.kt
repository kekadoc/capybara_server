package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.data.source.network.model.ContactDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddPublicContactResponseDto(
    @SerialName("contact")
    val contact: ContactDto,
)