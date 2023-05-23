package com.kekadoc.project.capybara.server.domain.model.message

data class MessageNotifications(
    val email: Boolean,
    val sms: Boolean,
    val app: Boolean,
    val messengers: Boolean,
) {

    companion object {

        val Default = MessageNotifications(
            email = false,
            sms = false,
            app = false,
            messengers = false,
        )

    }

}