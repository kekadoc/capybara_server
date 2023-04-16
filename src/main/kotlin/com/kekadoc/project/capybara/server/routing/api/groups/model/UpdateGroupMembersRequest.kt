package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class UpdateGroupMembersRequest(
    val members: Set<Identifier>,
)