package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    @SerialName("id")
    val id: String = emptyString(),
    @SerialName("userContactId")
    val userContactId: String = emptyString(),
    @SerialName("communications")
    val communications: Map<String, String> = emptyMap(),
)