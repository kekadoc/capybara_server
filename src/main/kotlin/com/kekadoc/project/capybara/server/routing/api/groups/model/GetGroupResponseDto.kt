package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.data.source.network.model.GroupDto
import kotlinx.serialization.Serializable

@Serializable
data class GetGroupResponseDto(
    val group: GroupDto,
)