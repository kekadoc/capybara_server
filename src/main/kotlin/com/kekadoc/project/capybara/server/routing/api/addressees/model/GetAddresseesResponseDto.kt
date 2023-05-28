package com.kekadoc.project.capybara.server.routing.api.addressees.model

import com.kekadoc.project.capybara.server.routing.model.group.GroupDto
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAddresseesResponseDto(
    @SerialName("users")
    val users: List<ProfileDto>,
    @SerialName("groups")
    val groups: List<GroupDto>,
)