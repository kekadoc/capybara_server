package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @Contextual
    @SerialName("id")
    val id: Identifier,
    @Contextual
    @SerialName("author_id")
    val authorId: Identifier,
    @SerialName("type")
    val type: Type,
    @SerialName("content")
    val content: Content,
) {

    enum class Type {
        DEFAULT,
        FOR_GROUP,
        FOR_USER,
    }


    @Serializable
    data class Content(
        @SerialName("title")
        val title: String?,
        @SerialName("text")
        val text: String,
        @SerialName("image")
        val image: String?,
    )

}