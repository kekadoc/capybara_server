package com.kekadoc.project.capybara.server.data.model

open class Communication(
    val name: String,
    val value: String,
) {

    data class Phone(
        val number: String,
    ) : Communication(name = "Phone", value = number)

    data class Email(
        val address: String,
    ) : Communication(name = "Email", value = address)

    data class Viber(
        val number: String,
    ) : Communication(name = "Viber", value = number)

    data class WhatsApp(
        val number: String,
    ) : Communication(name = "WhatsApp", value = number)

    data class Telegram(
        val number: String,
    ) : Communication(name = "Telegram", value = number)

}