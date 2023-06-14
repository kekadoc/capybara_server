package com.kekadoc.project.capybara.server.routing.api.groups.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateGroupNameRequestDto(
    @SerialName("name")
    val name: String,
)