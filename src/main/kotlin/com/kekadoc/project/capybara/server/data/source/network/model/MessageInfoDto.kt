package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageInfoDto(
    @SerialName("message")
    val message: MessageDto,
    @SerialName("addressee_groups")
    val addresseeGroups: List<GroupInfo>,
    @SerialName("addressee_users")
    val addresseeUsers: List<FromUserInfo>,
    @SerialName("status")
    val status: Status,
) {

    @Serializable
    data class GroupInfo(
        @Contextual
        @SerialName("id")
        val id: Identifier,
        @SerialName("name")
        val name: String,
        @SerialName("members")
        val members: List<FromUserInfo>,
    )

    @Serializable
    data class FromUserInfo(
        @Contextual
        @SerialName("user_id")
        val userId: Identifier,
        @SerialName("received")
        val received: Boolean,
        @SerialName("read")
        val read: Boolean,
        @SerialName("answer")
        val answer: String?,
    )

    enum class Status {
        UNDEFINED,
        /**
         * Принято сервером
         */
        RECEIVED,
        /**
         * Отправлено
         */
        SENT,
        /**
         * Отменено
         */
        CANCELED,
        /**
         * Заблокировано сервером
         */
        REJECTED,
        /**
         * Ошибка отправки
         */
        FAILED
    }

}