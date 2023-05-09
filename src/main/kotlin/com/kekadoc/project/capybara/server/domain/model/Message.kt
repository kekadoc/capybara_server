package com.kekadoc.project.capybara.server.domain.model

data class Message(
    val id: Identifier,
    val authorId: Identifier,
    val type: Type,
    val content: Content,
    val addresseeUserIds: List<Identifier>,
    val addresseeGroupIds: List<Identifier>,
    val actions: Actions? = null,
    val notifications: Notifications = Notifications.Default,
) {

    data class Actions(
        val action1: String? = null,
        val action2: String? = null,
        val action3: String? = null,
    )

    data class Notifications(
        val email: Boolean,
        val sms: Boolean,
        val app: Boolean,
        val messengers: Boolean,
    ) {

        companion object {

            val Default = Notifications(
                email = false,
                sms = false,
                app = false,
                messengers = false,
            )

        }

    }

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