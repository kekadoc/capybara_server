package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
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
    @SerialName("notifications")
    val notifications: Notifications? = null,
    @SerialName("actions")
    val actions: Actions? = null
) {

    @Serializable
    data class Actions(
        @SerialName("action_1")
        val action1: String? = null,
        @SerialName("action_2")
        val action2: String? = null,
        @SerialName("action_3")
        val action3: String? = null,
    )

    @Serializable
    data class Notifications(
        @SerialName("email")
        val email: Boolean = false,
        @SerialName("sms")
        val sms: Boolean = false,
        @SerialName("app")
        val app: Boolean = false,
        @SerialName("messengers")
        val messengers: Boolean = false,
    )

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