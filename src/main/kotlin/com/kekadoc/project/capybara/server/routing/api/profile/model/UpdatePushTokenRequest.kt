package com.kekadoc.project.capybara.server.routing.api.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenRequest(
    val token: String,
)