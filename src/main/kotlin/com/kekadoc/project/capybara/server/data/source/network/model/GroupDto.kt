package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: Identifier,
    val name: String,
    val members: List<ProfileDto>,
)