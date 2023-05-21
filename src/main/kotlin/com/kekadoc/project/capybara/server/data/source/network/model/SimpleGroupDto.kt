package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleGroupDto(
    @Contextual
    @SerialName("id")
    val id: Identifier,
    @SerialName("name")
    val name: String,
)