package com.kekadoc.project.capybara.server.domain.model

data class Communication(
    val type: Type,
    val value: String,
) {

    companion object {
        operator fun invoke(type: String, value: String): Communication {
            return Communication(
                type = Type.values.find { next -> next.name == type } ?: Type.Unknown(type),
                value = value,
            )
        }
    }

    sealed class Type(
        val name: String,
    ) {
        companion object {
            val values: List<Type> = listOf(
                Phone,
                Email,
                Viber,
                WhatsApp,
                Telegram,
            )
        }
        object Phone : Type("PHONE")
        object Email : Type("EMAIL")
        object Viber : Type("VIBER")
        object WhatsApp : Type("WHATS_APP")
        object Telegram : Type("TELEGRAM")
        class Unknown(code: String) : Type(code)

        override fun toString(): String = name
    }

}