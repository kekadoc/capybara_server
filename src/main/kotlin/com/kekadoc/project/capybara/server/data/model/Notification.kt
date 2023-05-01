package com.kekadoc.project.capybara.server.data.model

data class Notification(
    val id: Identifier,
    val authorId: Identifier,
    val type: Type,
    val content: Content,
    val addresseeUserIds: List<Identifier>,
    val addresseeGroupIds: List<Identifier>,
) {

    enum class Type {
        DEFAULT,
        FOR_GROUP,
        FOR_USER,
    }


    data class Content(
        val title: String?,
        val text: String,
        val image: String?,
    )

}