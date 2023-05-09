package com.kekadoc.project.capybara.server.domain.model

data class MessageInfo(
    val message: Message,
    val addresseeGroups: List<GroupInfo>,
    val addresseeUsers: List<FromUserInfo>,
    val status: Status,
) {

    data class GroupInfo(
        val id: Identifier,
        val name: String,
        val members: List<FromUserInfo>,
    )

    data class FromUserInfo(
        val userId: Identifier,
        val received: Boolean,
        val read: Boolean,
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