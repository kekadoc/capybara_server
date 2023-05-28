package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.routing.model.group.SimpleGroupDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetGroupListResponseDto(
    @SerialName("groups")
    val groups: List<SimpleGroupDto>,
)