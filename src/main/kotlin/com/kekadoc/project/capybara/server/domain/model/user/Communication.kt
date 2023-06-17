package com.kekadoc.project.capybara.server.domain.model.user

data class Communication(
    val type: Type,
    val value: String,
    val approved: Boolean? = null,
) {

    companion object {

        operator fun invoke(
            type: String,
            value: String,
            approved: Boolean? = null,
        ): Communication = Communication(
            type = Type.from(type),
            value = value,
            approved = approved,
        )

    }

    sealed class Type(
        val name: String,
        val requireApprove: Boolean,
    ) {
        companion object {

            val values: List<Type> = listOf(
                Phone,
                Email,
                Viber,
                WhatsApp,
                Telegram,
            )

            fun from(name: String): Type = values.find { next -> next.name == name } ?: Unknown(name)

        }

        object Email : Type("EMAIL", true)
        object Phone : Type("PHONE", false)
        object Viber : Type("VIBER", false)
        object WhatsApp : Type("WHATS_APP", false)
        object Telegram : Type("TELEGRAM", false)
        class Unknown(code: String) : Type(code, false)

        override fun toString(): String = name

    }

}