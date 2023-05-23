package com.kekadoc.project.capybara.server.routing.api.messages.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateReceivedMessageAnswerRequestDto(
    @SerialName("answer_ids")
    val answerIds: List<Long>,
)