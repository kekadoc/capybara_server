package com.kekadoc.project.capybara.server.routing.api.notifications.model

import kotlinx.serialization.Serializable

@Serializable
data class PostReceivedMessageAnswerRequest(
    val answer: String,
)