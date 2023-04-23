package com.kekadoc.project.capybara.server.data.model

data class Communication(
    val type: Type,
    val value: String,
) {

    companion object {
        operator fun invoke(type: String, value: String): Communication {
            return Communication(
                type = Type.values().find { next -> next.name == type } ?: Type.UNKNOWN,
                value = value,
            )
        }
    }

    enum class Type {
        PHONE,
        EMAIL,
        VIBER,
        WHATS_APP,
        TELEGRAM,
        UNKNOWN,
    }

}