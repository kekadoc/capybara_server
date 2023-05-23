package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.routing.model.group.GroupDto
import kotlinx.serialization.Serializable

@Serializable
data class GetGroupResponseDto(
    val group: GroupDto,
)