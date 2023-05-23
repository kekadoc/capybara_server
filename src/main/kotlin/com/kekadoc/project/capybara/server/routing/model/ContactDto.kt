package com.kekadoc.project.capybara.server.routing.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactDto(
    @Contextual
    @SerialName("id")
    val id: Identifier,
    @SerialName("type")
    val type: String,
    @SerialName("profile")
    val profile: ProfileDto,
    @SerialName("communications")
    val communications: Map<String, String>,
)