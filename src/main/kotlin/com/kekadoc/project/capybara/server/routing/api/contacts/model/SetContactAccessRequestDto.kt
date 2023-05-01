package com.kekadoc.project.capybara.server.routing.api.contacts.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetContactAccessRequestDto(
    @SerialName("values")
    val values: List<Value>
) {

    @Serializable
    data class Value(
        @Contextual
        @SerialName("user_id")
        val userId: Identifier,
        @Contextual
        @SerialName("contact_id")
        val contactId: Identifier,
        @SerialName("is_access")
        val isAccess: Boolean,
    )

}