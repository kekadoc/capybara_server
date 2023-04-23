package com.kekadoc.project.capybara.server.data.model

data class Notification(
    val id: Identifier,
    val author: User,
    val type: Type,
    val content: Content,
) {

    enum class Type {
        FOR_GROUP,
        FOR_USER,
    }


    data class Content(
        val title: String?,
        val text: String,
        val image: String?,
    )

}