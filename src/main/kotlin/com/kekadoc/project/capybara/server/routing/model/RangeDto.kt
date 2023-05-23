package com.kekadoc.project.capybara.server.routing.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RangeDto(
    @SerialName("from")
    val from: Int,
    @SerialName("count")
    val count: Int,
)