package com.kekadoc.project.capybara.server.routing.api.groups.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetGroupWithMembersListRequestDto(
    @SerialName("ids")
    val ids: List<String>,
)