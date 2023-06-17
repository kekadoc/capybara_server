package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("members")
    val members: List<@Contextual Identifier>,
    @SerialName("type")
    val type: String,
)