package com.kekadoc.project.capybara.server.routing.api.addressees.model

import com.kekadoc.project.capybara.server.routing.model.group.GroupDto
import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class GetAddresseesResponse(
    val users: List<ProfileDto>,
    val groups: List<GroupDto>,
)