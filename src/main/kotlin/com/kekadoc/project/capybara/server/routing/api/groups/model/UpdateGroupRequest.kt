package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UpdateGroupRequest(
    val name: String,
    val members: Set<@Contextual Identifier>,
)