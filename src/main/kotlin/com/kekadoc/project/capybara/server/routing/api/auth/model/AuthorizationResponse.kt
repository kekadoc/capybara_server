package com.kekadoc.project.capybara.server.routing.api.auth.model

import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationResponse(
    val token: String,
    val profile: ProfileDto,
)