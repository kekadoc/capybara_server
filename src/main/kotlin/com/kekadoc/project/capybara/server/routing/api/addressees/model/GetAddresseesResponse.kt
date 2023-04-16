package com.kekadoc.project.capybara.server.routing.api.addressees.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class GetAddresseesResponse(
    val users: List<Identifier>,
    val groups: List<Identifier>,
)