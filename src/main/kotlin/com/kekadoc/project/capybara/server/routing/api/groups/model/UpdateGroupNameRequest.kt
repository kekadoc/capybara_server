package com.kekadoc.project.capybara.server.routing.api.groups.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateGroupNameRequest(
    val name: String,
)