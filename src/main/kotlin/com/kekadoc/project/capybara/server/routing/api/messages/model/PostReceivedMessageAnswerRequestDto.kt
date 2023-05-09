package com.kekadoc.project.capybara.server.routing.api.messages.model

import kotlinx.serialization.Serializable

@Serializable
data class PostReceivedMessageAnswerRequestDto(
    val answer: String,
)