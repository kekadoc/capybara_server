package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.data.model.Group
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupResponse(
    val group: Group
)