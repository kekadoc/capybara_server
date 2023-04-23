package com.kekadoc.project.capybara.server.routing.api.addressees.model

import com.kekadoc.project.capybara.server.data.source.network.model.GroupDto
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class GetAddresseesResponse(
    val users: List<ProfileDto>,
    val groups: List<GroupDto>,
)