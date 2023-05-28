package com.kekadoc.project.capybara.server.routing.api.groups.model

import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetGroupMembersResponseDto(
    @SerialName("members")
    val members: List<ProfileDto>
)