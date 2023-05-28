package com.kekadoc.project.capybara.server.routing.api.messages.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.routing.model.message.MessageNotificationsDto
import com.kekadoc.project.capybara.server.routing.model.message.MessageTypeDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequestDto(
    @SerialName("type")
    val type: MessageTypeDto,
    @SerialName("title")
    val title: String? = null,
    @SerialName("text")
    val text: String,
    @SerialName("actions")
    val actions: List<String>? = null,
    @SerialName("is_multi_answer")
    val isMultiAnswer: Boolean = false,
    @SerialName("addressee_users")
    val addresseeUsers: List<@Contextual Identifier> = emptyList(),
    @SerialName("addressee_groups")
    val addresseeGroups: List<AddresseeGroup> = emptyList(),
    @SerialName("notifications")
    val notifications: MessageNotificationsDto? = null,
) {

    @Serializable
    data class AddresseeGroup(
        @Contextual
        @SerialName("group_id")
        val groupId: Identifier,
        @SerialName("members_ids")
        val membersIds: List<@Contextual Identifier>? = null
    )

}