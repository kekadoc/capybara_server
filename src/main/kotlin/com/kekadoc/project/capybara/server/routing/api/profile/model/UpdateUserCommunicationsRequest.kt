package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCommunicationsRequest(
    val values: Map<String, String>,
)