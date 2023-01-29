package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageContent(
    @SerialName("title")
    val title: String = emptyString(),
    @SerialName("text")
    val text: String = emptyString(),
    @SerialName("image")
    val image: String = emptyString()
) {
    
    companion object {
        val Empty: MessageContent = MessageContent()
    }
}