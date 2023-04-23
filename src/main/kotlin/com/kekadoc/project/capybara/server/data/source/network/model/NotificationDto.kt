package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: Identifier,
    val author: ProfileDto,
    val type: Type,
    val content: Content,
) {

    enum class Type {
        FOR_GROUP,
        FOR_USER,
    }


    @Serializable
    data class Content(
        val title: String?,
        val text: String,
        val image: String?,
    )

}